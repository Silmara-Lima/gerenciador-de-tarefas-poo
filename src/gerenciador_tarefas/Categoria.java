package gerenciador_tarefas;

/**
 * Classe que representa a Categoria das tarefas no sistema.
 * Demonstra a relação de associação em POO, onde uma categoria pertence a um Usuário
 */
public class Categoria {

    // Atributos privados para garantir o encapsulamento
    private Integer id;
    private String nomeCategoria;
    
    // Relação de Associação: Uma categoria está associada a um utilizador
    private Usuario usuario;

    /**
     * Construtor padrão.
     */
    public Categoria() {
    }

    /**
     * Construtor completo para instanciar a Categoria com dados vindos do banco de dados.
     * * @param id Identificador único da categoria.
     * @param nomeCategoria Nome descritivo da categoria (ex: Trabalho, Estudos).
     * @param usuario Objeto do Usuário que criou esta categoria.
     */
    public Categoria(Integer id, String nomeCategoria, Usuario usuario) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
        this.usuario = usuario; // Uso do 'this' para distinguir o atributo do parâmetro
    }

    // ==========================================
    // MÉTODOS GETTERS E SETTERS
    // ==========================================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
