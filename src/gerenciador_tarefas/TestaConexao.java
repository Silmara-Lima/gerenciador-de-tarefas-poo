package gerenciador_tarefas;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe utilitária para testar visualmente se o Java consegue ler o arquivo
 * 'config.properties' e estabelecer a conexão com o MySQL através da ConfigBanco.
 */
public class TestaConexao {

    public static void main(String[] args) {
        System.out.println("Lendo arquivo de configuração e tentando conectar ao MySQL...");

        // O try-with-resources tenta abrir a conexão usando a sua nova lógica segura
        try (Connection conexao = ConfigBanco.createConnection()) {
            
            // Se não disparar nenhuma exceção (erro), o fluxo chega aqui com sucesso
            if (conexao != null && !conexao.isClosed()) {
                System.out.println("\n=========================================");
                System.out.println("  CONEXÃO ESTABELECIDA COM SUCESSO! 🚀");
                System.out.println("=========================================");
                System.out.println("1. O arquivo 'config.properties' foi lido corretamente.");
                System.out.println("2. As credenciais do banco estão válidas.");
                System.out.println("3. O sistema está pronto para rodar o MainApp.");
                System.out.println("=========================================");
            }
            
        } catch (SQLException e) {
            // Caso falte o arquivo, a senha esteja errada ou o banco esteja desligado
            System.err.println("\n[ERRO] FALHA CRÍTICA DE INFRAESTRUTURA!");
            System.err.println("Motivo real do problema: " + e.getMessage());
            System.err.println("\nPassos para correção:");
            System.err.println("- Verifique se o arquivo 'config.properties' está na raiz do projeto (fora de src).");
            System.err.println("- Verifique se digitou a senha correta dentro do arquivo.");
            System.err.println("- Certifique-se de que o seu servidor MySQL (Wamp/XAMPP/Workbench) está ligado.");
        }
    }
}