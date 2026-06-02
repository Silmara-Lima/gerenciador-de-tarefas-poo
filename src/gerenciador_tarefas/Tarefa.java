package gerenciador_tarefas;

import java.time.LocalDateTime;

/**
 * Classe principal que representa uma Tarefa no sistema.
 * Demonstra relações complexas de POO através da associação direta 
 * com as classes Usuario e Categoria, além do uso do Enum de status
 */
public class Tarefa {

    // Atributos privados (Encapsulamento)
    private Integer id;
    private String titulo;
    private String descricao;
    private String prioridade;
    private LocalDateTime prazo;
    private LocalDateTime dataCriacao;

    // Uso do Enum que acabamos de criar
    private StatusTarefa status;

    // Relações entre classes (Associações exigidas no roteiro)
    private Usuario usuario;       // Relação 1:1 - Toda tarefa precisa de um Usuário dono
    private Categoria categoria;   // Relação 1:1 - Uma tarefa pode ter uma Categoria associada

    /**
     * Construtor padrão da classe.
     * Define automaticamente o status inicial como PENDENTE ao criar uma nova tarefa.
     */
    public Tarefa() {
        this.status = StatusTarefa.PENDENTE;
    }

    /**
     * Construtor completo para instanciar a Tarefa com dados vindos do banco de dados.
     */
    public Tarefa(Integer id, String titulo, String descricao, StatusTarefa status, String prioridade, 
                  LocalDateTime prazo, LocalDateTime dataCriacao, Usuario usuario, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.prioridade = prioridade;
        this.prazo = prazo;
        this.dataCriacao = dataCriacao;
        this.usuario = usuario;
        this.categoria = categoria;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDateTime prazo) {
        this.prazo = prazo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
