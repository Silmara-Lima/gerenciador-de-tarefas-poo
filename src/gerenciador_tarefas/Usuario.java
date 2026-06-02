package gerenciador_tarefas;

import java.time.LocalDateTime;

/**
 * Classe que representa a entidade Usuário no sistema de gerenciamento de tarefas.
 * Aplica o pilar do Encapsulamento da POO, mantendo os atributos privados e
 * controlando o acesso através de métodos públicos getters e setters.
 */
public class Usuario {
    
    // Atributos privados (segurança e encapsulamento)
    private Integer id;
    private String nome;
    private String email;
    private String senha; // Armazenará a senha para autenticação
    private LocalDateTime dataCadastro;

    /**
     * Construtor padrão da classe.
     * Necessário para inicializações limpas e compatibilidade com padrões de projeto
     */
    public Usuario() {
    }

    /**
     * Construtor completo para instanciar o Usuário com dados preexistentes (vindos do Banco).
     * * @param id Identificador único do usuário.
     * @param nome Nome completo do usuário.
     * @param email Endereço de e-mail único (utilizado como login).
     * @param senha Senha de acesso do usuário.
     * @param dataCadastro Data e hora em que a conta foi criada.
     */
    public Usuario(Integer id, String nome, String email, String senha, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = dataCadastro;
    }

    // ==========================================
    // MÉTODOS GETTERS E SETTERS (Acessores)
    // ==========================================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
