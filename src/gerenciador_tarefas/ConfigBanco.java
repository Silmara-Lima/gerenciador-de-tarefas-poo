package gerenciador_tarefas;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe responsável por configurar e fornecer conexões com o banco de dados.
 * Utiliza um arquivo externo .properties para proteger credenciais sensíveis.
 */
public class ConfigBanco {

    /**
     * Abre e retorna uma conexão ativa com o banco de dados MySQL,
     * carregando as credenciais de forma segura a partir de um arquivo externo.
     * * @return Connection objeto de conexão pronto para uso.
     * @throws SQLException caso ocorra erro de conexão.
     */
    public static Connection createConnection() throws SQLException {
        Properties props = new Properties();
        
        // Carrega o arquivo de propriedades dinamicamente
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            // Caso o arquivo não seja encontrado, lança um aviso claro
            throw new SQLException("Arquivo 'config.properties' não encontrado na raiz do projeto. Verifique sua configuração local.", e);
        }

        // Recupera os valores de dentro do arquivo de texto
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }
}