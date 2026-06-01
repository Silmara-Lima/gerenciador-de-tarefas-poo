package gerenciador_tarefas;

/**
 * Enumeração que define os estados fixos e possíveis de uma tarefa no sistema.
 * Garante a integridade dos dados entre a aplicação Java e o ENUM do MySQL,
 * evitando erros de digitação e valores inválidos.
 */
public enum StatusTarefa {
    
    // Constantes do Enum (por convenção em Java, declaradas em MAIÚSCULO)
    PENDENTE,
    CONCLUIDO,
    EM_ATRASO;
}