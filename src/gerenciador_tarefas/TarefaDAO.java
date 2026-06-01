package gerenciador_tarefas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Data Access Object (DAO) para a entidade Tarefa.
 * Centraliza e isola todas as instruções SQL de INSERT, SELECT, UPDATE e DELETE
 * da tabela 'tarefas', utilizando boas práticas de gestão de recursos.
 */
public class TarefaDAO {

    /**
     * Insere uma nova tarefa associada a um utilizador no banco de dados.
     * * @param tarefa Objeto Tarefa preenchido com os dados a persistir.
     * @return boolean True se a inserção foi bem-sucedida, false caso contrário.
     */
    public boolean salvar(Tarefa tarefa) {
        String sql = "INSERT INTO tarefas (titulo, descricao, status, prioridade, prazo, data_criacao, usuario_id, categoria_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConfigBanco.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            
            // Garante que o status inicial está preenchido e converte o Enum para String
            if (tarefa.getStatus() == null) {
                tarefa.setStatus(StatusTarefa.PENDENTE);
            }
            stmt.setString(3, tarefa.getStatus().name());
            
            stmt.setString(4, tarefa.getPrioridade());
            stmt.setTimestamp(5, Timestamp.valueOf(tarefa.getPrazo()));
            
            // Define a data de criação atual se não vier definida
            if (tarefa.getDataCriacao() == null) {
                tarefa.setDataCriacao(LocalDateTime.now());
            }
            stmt.setTimestamp(6, Timestamp.valueOf(tarefa.getDataCriacao()));
            
            // Vincula o ID do utilizador dono da tarefa (Chave Estrangeira)
            stmt.setInt(7, tarefa.getUsuario().getId());

            // Tratamento preventivo para chaves estrangeiras opcionais (evita NullPointerException)
            if (tarefa.getCategoria() != null && tarefa.getCategoria().getId() != null) {
                stmt.setInt(8, tarefa.getCategoria().getId());
            } else {
                // Se a tarefa não tiver categoria, insere NULL no banco de dados
                stmt.setNull(8, Types.INTEGER);
            }

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar tarefa: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera todas as tarefas de um utilizador específico, incluindo os dados da categoria se houver.
     * Atende ao requisito de manipulação de listas e coleções dinâmicas do roteiro.
     * * @param usuarioId ID do utilizador logado para filtrar apenas as suas tarefas.
     * @return List<Tarefa> Lista contendo as tarefas encontradas (pode retornar uma lista vazia).
     */
    public List<Tarefa> listarPorUsuario(int usuarioId) {
        List<Tarefa> lista = new ArrayList<>();
        // Fazemos um LEFT JOIN para trazer o nome da categoria associada à tarefa de forma eficiente
        String sql = "SELECT t.*, c.nome_categoria FROM tarefas t " +
                     "LEFT JOIN categorias c ON t.categoria_id = c.id " +
                     "WHERE t.usuario_id = ?";

        try (Connection conn = ConfigBanco.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tarefa tarefa = new Tarefa();
                    tarefa.setId(rs.getInt("id"));
                    tarefa.setTitulo(rs.getString("titulo"));
                    tarefa.setDescricao(rs.getString("descricao"));
                    
                    // Converte a String de volta para o ENUM do Java de forma segura
                    tarefa.setStatus(StatusTarefa.valueOf(rs.getString("status").toUpperCase()));
                    
                    tarefa.setPrioridade(rs.getString("prioridade"));
                    tarefa.setPrazo(rs.getTimestamp("prazo").toLocalDateTime());
                    tarefa.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());

                    // Reconstrói o objeto Categoria se ele existir na linha retornada
                    int categoriaId = rs.getInt("categoria_id");
                    if (!rs.wasNull()) {
                        Categoria cat = new Categoria();
                        cat.setId(categoriaId);
                        cat.setNomeCategoria(rs.getString("nome_categoria"));
                        tarefa.setCategoria(cat); // Associa o objeto Categoria dentro do objeto Tarefa
                    }

                    lista.add(tarefa); // Alimenta a coleção dinamicamente
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar tarefas do utilizador: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Atualiza os dados de uma tarefa existente.
     * * @param tarefa Objeto contendo as modificações e o ID da tarefa alvo.
     * @return boolean True se a atualização afetou o registro no banco.
     */
    public boolean atualizar(Tarefa tarefa) {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ?, status = ?, prioridade = ?, prazo = ?, categoria_id = ? WHERE id = ?";

        try (Connection conn = ConfigBanco.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getStatus().name());
            stmt.setString(4, tarefa.getPrioridade());
            stmt.setTimestamp(5, Timestamp.valueOf(tarefa.getPrazo()));

            if (tarefa.getCategoria() != null && tarefa.getCategoria().getId() != null) {
                stmt.setInt(6, tarefa.getCategoria().getId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setInt(7, tarefa.getId());

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tarefa: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina permanentemente uma tarefa através do seu identificador.
     * * @param id Identificador único da tarefa a ser removida.
     * @return boolean True se a exclusão foi concluída.
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM tarefas WHERE id = ?";

        try (Connection conn = ConfigBanco.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao remover tarefa: " + e.getMessage());
            return false;
        }
    }
}