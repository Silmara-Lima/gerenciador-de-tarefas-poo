package gerenciador_tarefas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Classe Data Access Object (DAO) para a entidade Usuario.
 * Encapsula de forma exclusiva todas as operações de manipulação de dados (CRUD)
 * e regras de persistência da tabela 'usuarios' no MySQL
 */
public class UsuarioDAO {

    /**
     * Insere um novo utilizador no banco de dados de forma persistente.
     * * @param usuario Objeto Usuario contendo os dados preenchidos na interface.
     * @return boolean Retorna true se o cadastro foi bem-sucedido e false caso falhe.
     */
    public boolean salvar(Usuario usuario) {
        // Comando SQL com marcadores (?) para evitar SQL Injection
        String sql = "INSERT INTO usuarios (nome, email, senha, data_cadastro) VALUES (?, ?, ?, ?)";

        // Try-with-resources: Abre a conexão e o PreparedStatement e garante o fecho automático
        try (Connection conn = ConfigBanco.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Substitui os marcadores (?) pelos valores reais do objeto
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha()); // Numa aplicação real, a senha iria criptografada
            
            // Define a data de cadastro atual caso o objeto não traga uma específica
            if (usuario.getDataCadastro() == null) {
                usuario.setDataCadastro(LocalDateTime.now());
            }
            stmt.setTimestamp(4, Timestamp.valueOf(usuario.getDataCadastro()));

            // Executa a inserção no banco de dados
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0; // Se alterou mais de 0 linhas, salvou com sucesso

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar utilizador: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida as credenciais do utilizador para controlo de acesso (Autenticação).
     * * @param email O e-mail digitado no login.
     * @param senha A senha digitada no login.
     * @return Usuario Retorna o objeto Usuario completo se as credenciais forem válidas, ou null caso falhe.
     */
    public Usuario autenticar(String email, String senha) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        
        try (Connection conn = ConfigBanco.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            // Executa a consulta e armazena o resultado no ResultSet
            try (ResultSet rs = stmt.executeQuery()) {
                // Se encontrar um registo correspondente
                if (rs.next()) {
                    Usuario usuarioLogado = new Usuario();
                    usuarioLogado.setId(rs.getInt("id"));
                    usuarioLogado.setNome(rs.getString("nome"));
                    usuarioLogado.setEmail(rs.getString("email"));
                    usuarioLogado.setSenha(rs.getString("senha"));
                    usuarioLogado.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
                    
                    return usuarioLogado; // Retorna o utilizador validado para manter na sessão
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro no processo de autenticação: " + e.getMessage());
        }
        
        return null; // Retorna null se o e-mail ou a senha estiverem incorretos
    }
}
