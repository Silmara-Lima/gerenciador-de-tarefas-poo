package gerenciador_tarefas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Data Access Object (DAO) para a entidade Categoria.
 * Gerencia o ciclo de vida e a persistência das categorias customizadas
 * pelos utilizadores na tabela 'categorias' do MySQL.
 */
public class CategoriaDAO {

    /**
     * Insere uma nova categoria associada a um utilizador no banco de dados.
     * * @param categoria Objeto Categoria contendo o nome e o utilizador dono.
     * @return boolean True se a inserção foi realizada com sucesso.
     */
    public boolean salvar(Categoria categoria) {
        String sql = "INSERT INTO categorias (nome_categoria, usuario_id) VALUES (?, ?)";

        try (Connection conn = ConfigBanco.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNomeCategoria());
            stmt.setInt(2, categoria.getUsuario().getId()); // Vincula a Chave Estrangeira do Usuário

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar categoria: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera e lista todas as categorias pertencentes a um utilizador específico.
     * Atende ao uso de coleções dinâmicas (List/ArrayList) exigido no roteiro.
     * * @param usuarioId ID do utilizador logado no sistema.
     * @return List<Categoria> Lista contendo as categorias encontradas.
     */
    public List<Categoria> listarPorUsuario(int usuarioId) {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias WHERE usuario_id = ? ORDER BY nome_categoria ASC";

        try (Connection conn = ConfigBanco.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setNomeCategoria(rs.getString("nome_categoria"));
                    
                    // Como boa prática, não precisamos reconstruir o usuário completo aqui para economizar memória,
                    // apenas instanciamos o objeto com o ID associado.
                    Usuario user = new Usuario();
                    user.setId(rs.getInt("usuario_id"));
                    categoria.setUsuario(user);

                    lista.add(categoria); // Adiciona o elemento à coleção
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Remove uma categoria do banco de dados pelo seu Identificador.
     * Nota de Sênior: Graças ao 'ON DELETE CASCADE' que configuramos no SQL,
     * ao apagar uma categoria, as tarefas vinculadas a ela terão o campo 'categoria_id' setado como NULL automaticamente.
     * * @param id Identificador único da categoria a ser excluída.
     * @return boolean True se a exclusão afetou o registro.
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM categorias WHERE id = ?";

        try (Connection conn = ConfigBanco.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir categoria: " + e.getMessage());
            return false;
        }
    }
}