# Projeto Final: Aplicativo de Gerenciamento de Tarefas
**Disciplina:** Programação Orientada a Objetos (POO) - Terceiro Período (P3)  
**Instituição:** Centro Universitário de João Pessoa (UNIPÊ)  
**Professor:** Tiago Emilio  

**Integrantes** 

Flavia Tavares do Nascimento - 42031141 - nflaviatavares@gmail.com

Luís Gustavo Rocha Gomes De Andrade - 42917107 - luis.lgdev@gmail.com

Luiz Carlos Souza Costa Cavadinha - 42782759 - luizcarlos.souzacosta@yahoo.com.br

Paulo Henrique Freire de Matos Barbosa - 45851671 - paulohenriquefmb@outlook.com

Silmara Pereira de Lima - 43999123 - silmara.pereiraspl@gmail.com

Vytor Gabryel Rodrigues de Araujo - 41884230 - vitugasbriel@gmail.com

---

## 1. Definição dos Requisitos

### Requisitos Funcionais (RF)

- **RF01 - Cadastro de Usuários:** O sistema deve permitir que novos usuários criem uma conta informando nome, e-mail exclusivo e senha.
- **RF02 - Autenticação de Usuários:** O sistema deve validar as credenciais (e-mail e senha) para liberar o acesso às funcionalidades protegidas.
- **RF03 - Gerenciamento de Categorias:** O usuário autenticado deve conseguir criar e listar categorias personalizadas para organizar as suas atividades.
- **RF04 - CRUD de Tarefas:** O sistema deve permitir que o usuário execute todas as operações básicas de gerenciamento de tarefas: Criar (Adicionar), Ler (Listar), Atualizar (Editar) e Excluir.
- **RF05 - Controle de Status e Prioridades:** O sistema deve permitir atribuir níveis de prioridade (Alta, Média, Baixa) e atualizar os estados das tarefas utilizando regras de negócio baseadas em estados (`PENDENTE`, `CONCLUIDO`, `EM_ATRASO`).
- **RF06 - Definição de Prazos e Lembretes:** O usuário deve poder inserir datas e horários limites para a conclusão de cada tarefa, além de configurar lembretes associados.

### Requisitos Não Funcionais (RNF)

- **RNF01 - Persistência de Dados:** Todos os dados gerados pela aplicação devem ser guardados de forma definitiva e íntegra em um Banco de Dados Relacional (MySQL).
- **RNF02 - Integridade Relacional Rigorosa:** O banco de dados utiliza restrições de integridade padrão (`RESTRICT`). Não é permitida a exclusão de um registro pai (como Usuário ou Categoria) se existirem registros filhos (Tarefas) vinculados a ele, garantindo a consistência das chaves estrangeiras e impedindo registros órfãos.
- **RNF03 - Segurança e Isolamento de Acesso:** O sistema deve isolar os dados por usuário, garantindo que um usuário logado não consiga visualizar ou alterar dados de outras contas.

---

## 2. Estrutura do Projeto e Arquitetura (POO)

A aplicação foi desenvolvida seguindo o padrão arquitetural **DAO (Data Access Object)**, que separa rigidamente as responsabilidades do sistema em camadas.

### Camadas do Sistema

1. **Camada de Modelo (`gerenciador_tarefas`):** Contém as entidades de negócio (`Usuario`, `Categoria`, `Tarefa`, `Lembrete`) mapeadas com encapsulamento completo (atributos privados, construtores e getters/setters).
2. **Camada de Persistência (DAOs):** Classes responsáveis por centralizar as instruções SQL (DML) de comunicação através do driver JDBC do MySQL (`UsuarioDAO`, `TarefaDAO`, `CategoriaDAO`).
3. **Camada de Infraestrutura (`ConfigBanco`):** Centraliza as configurações do driver JDBC e atua como uma Factory (fábrica) de conexões isolada, lendo credenciais de forma segura a partir de um arquivo externo.
4. **Camada de Visão/Lógica (`MainApp`):** Ponto de entrada do sistema contendo menus estruturados via console com laços de repetição e estruturas de decisão.

### Relacionamentos entre Objetos Aplicados

- **Associação 1:N (Um-para-Muitos):** Um `Usuario` pode possuir múltiplas instâncias de `Tarefa` e `Categoria` associadas ao seu ID. Uma `Tarefa` pode possuir múltiplos `Lembretes` vinculados.
- **Associação 1:1 (Um-para-Um):** Cada `Tarefa` possui obrigatoriamente um `Usuario` dono e pode fazer referência a uma única `Categoria`.

---

## 3. Tecnologias Utilizadas

| Tecnologia | Detalhe |
|---|---|
| **Linguagem** | Java (JDK 17 ou superior) |
| **Banco de Dados** | MySQL Server (acessado via MySQL Workbench) |
| **Conector** | Driver JDBC (MySQL Connector/J - Platform Independent) |
| **IDE** | Eclipse IDE |

---

## 4. Manual do Usuário e Guia de Execução

### Pré-requisitos do Sistema

Antes de iniciar a configuração, certifique-se de ter instalado em sua máquina:

- **Java Development Kit (JDK):** Versão 17 ou superior.
- **IDE Eclipse:** Configurada para projetos Java SE.
- **MySQL Server & MySQL Workbench:** Ativos e rodando na porta padrão `3306`.
- **Driver JDBC:** Arquivo `mysql-connector-j-9.7.0.jar` (ou equivalente) baixado.

---

### Passo 1 — Preparação do Banco de Dados (Workbench)

1. Abra o **MySQL Workbench** e conecte-se à sua instância local.
2. Abra uma nova aba de consultas (`Ctrl + T`) e execute o script DDL fornecido no arquivo `bd poo.sql` para criar o banco de dados `gerenciamento` e suas respectivas tabelas (`usuarios`, `categorias`, `tarefas` e `lembretes`).
3. Atualize o painel de *Schemas* e certifique-se de que as 4 tabelas foram geradas corretamente.

---

### Passo 2 — Configuração do Ambiente Java

Para isolar suas credenciais locais do repositório público (utilizando boas práticas de Engenharia de Software), o projeto utiliza uma arquitetura baseada em propriedades externas:

1. Na raiz do seu projeto no Eclipse (no mesmo nível da pasta `src`, e **nunca dentro dela**), crie um arquivo de texto simples chamado **`config.properties`**.
2. Insira as propriedades de conexão com as suas credenciais locais:

```properties
db.url=jdbc:mysql://localhost:3306/gerenciamento
db.user=root
db.password=SUA_SENHA_DO_WORKBENCH_AQUI
```

> ⚠️ Certifique-se de que o arquivo `.gitignore` também está criado na raiz, contendo a linha `config.properties`, para impedir o envio acidental da sua senha para o GitHub.

---

### Passo 3 — Estrutura de Pastas Esperada no Eclipse

```
MeuProjetoJava/
├── config.properties         (configuração local - senha)
├── .gitignore                (filtro de arquivos do Git)
├── README.md                 (esta documentação)
└── src/
    └── gerenciador_tarefas/
        ├── modelo/           (Usuario, Categoria, Tarefa, Lembrete)
        ├── persistencia/     (DAOs)
        └── MainApp.java      (Classe Principal)
```

---

### Passo 4 — Vinculando o Driver JDBC (Build Path)

O Java necessita do driver como um tradutor para o MySQL. Adicione-o ao projeto seguindo estas etapas no Eclipse:

1. Clique com o botão direito sobre o nome do projeto → **Build Path** → **Configure Build Path...**
2. Acesse a aba **Libraries**, selecione **Classpath** (se aplicável na sua versão do Eclipse) e clique em **Add External JARs...**
3. Escolha o arquivo do driver do MySQL (`mysql-connector-j`) previamente baixado.
4. Clique em **Apply and Close**.

---

### Passo 5 — Execução e Fluxo de Testes

#### Homologação da Conexão (`TestaConexao.java`)

Antes de abrir a aplicação completa, execute a classe `TestaConexao.java` (botão direito → **Run As** → **Java Application**).

**Resultado esperado:** O console exibirá uma mensagem de sucesso confirmando que o Java conseguiu ler o arquivo de propriedades e abrir um canal de comunicação com o MySQL

#### Execução da Aplicação (`MainApp.java`)

Com a conexão validada, execute a classe principal `MainApp.java` e interaja com o sistema pelo console seguindo o fluxo abaixo:

```
[Menu Inicial]
├── Opcao 1: Cadastrar Usuario (Nome, E-mail unico e Senha)
└── Opcao 2: Efetuar Login (Autenticacao de Credenciais)
      │
      └──► [Menu Principal do Usuario Autenticado]
              ├── Opcao 1: Criar Nova Categoria (Ex: "Faculdade", "Trabalho")
              ├── Opcao 2: Listar Minhas Categorias (Exibe os IDs numericos)
              ├── Opcao 3: Criar Nova Tarefa
              │     │      * Bloqueia o fluxo se nao houver categorias cadastradas
              │     │      * Rejeita IDs invalidos e formatos incorretos de data (try-catch)
              │     └──    (Pede Titulo, Descricao, Prioridade, Prazo e ID da Categoria)
              ├── Opcao 4: Listar Minhas Tarefas (Exibe dados e status atual)
              ├── Opcao 5: Editar / Atualizar Tarefa
              │     │      * Garante a existencia previa do ID e trata erro de digitacao da data
              │     └──    (Altera titulo, descricao, prioridade, status e prazo)
              ├── Opcao 6: Excluir uma Tarefa (Remocao fisica por ID)
              └── Opcao 7: Logout (Retorna ao Menu Inicial)
```
