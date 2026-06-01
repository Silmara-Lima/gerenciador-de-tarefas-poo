package gerenciador_tarefas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal da aplicação (Ponto de Entrada).
 * Orquestra a lógica de negócio através de menus interativos via console,
 * integrando as entidades de modelo com a camada de persistência (DAOs).
 */
public class MainApp {

    // Instanciação dos DAOs para manipulação do banco de dados
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private static final TarefaDAO tarefaDAO = new TarefaDAO();
    
    // Scanner global para leitura de dados do teclado
    private static final Scanner scanner = new Scanner(System.in);
    
    // Formatador padrão para exibição e leitura de datas
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    // Variável para armazenar o utilizador que está atualmente logado na sessão
    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  BEM-VINDO AO GERENCIADOR DE TAREFAS   ");
        System.out.println("========================================");

        boolean executarSistema = true;

        // Estrutura de repetição principal do sistema
        while (executarSistema) {
            if (usuarioLogado == null) {
                // Se não houver utilizador logado, exibe menu de acesso básico
                exibirMenuAcesso();
            } else {
                // Se o utilizador estiver autenticado, exibe o painel de controlo
                exibirMenuPrincipal();
            }
        }
        
        scanner.close();
        System.out.println("Sistema encerrado com sucesso. Até logo!");
    }

    /**
     * Menu inicial para utilizadores não autenticados.
     */
    private static void exibirMenuAcesso() {
        System.out.println("\n--- MENU DE ACESSO ---");
        System.out.println("1. Cadastrar Nova Conta");
        System.out.println("2. Fazer Login");
        System.out.println("3. Sair do Aplicativo");
        System.out.print("Escolha uma opção: ");
        
        int opcao = lerOpcaoInt();

        switch (opcao) {
            case 1:
                executarCadastro();
                break;
            case 2:
                executarLogin();
                break;
            case 3:
                System.exit(0); // Encerra a JVM imediatamente
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
        }
    }

    /**
     * Painel de controlo principal libertado após a autenticação bem-sucedida.
     */
    private static void exibirMenuPrincipal() {
        System.out.println("\n========================================");
        System.out.println("Olá, " + usuarioLogado.getNome() + "! O que deseja fazer?");
        System.out.println("========================================");
        System.out.println("1. Criar Nova Categoria");
        System.out.println("2. Listar Minhas Categorias");
        System.out.println("3. Criar Nova Tarefa");
        System.out.println("4. Listar Minhas Tarefas");
        System.out.println("5. Editar / Atualizar Tarefa");
        System.out.println("6. Eliminar uma Tarefa");
        System.out.println("7. Fazer Logout (Trocar de conta)");
        System.out.print("Escolha uma opção: ");

        int opcao = lerOpcaoInt();

        switch (opcao) {
            case 1:
                criarCategoria();
                break;
            case 2:
                listarCategorias();
                break;
            case 3:
                criarTarefa();
                break;
            case 4:
                listarTarefas();
                break;
            case 5:
                editarTarefa();
                break;
            case 6:
                eliminarTarefa();
                break;
            case 7:
                System.out.println("Efetuando logout...");
                usuarioLogado = null; // Limpa a sessão do utilizador
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    // ==========================================
    // OPERAÇÕES DE USUÁRIO (Acesso)
    // ==========================================

    private static void executarCadastro() {
        System.out.println("\n--- CADASTRO DE NOVO USUÁRIO ---");
        System.out.print("Nome Completo: ");
        String nome = scanner.nextLine();
        System.out.print("E-mail (Login único): ");
        String email = scanner.nextLine();
        System.out.print("Senha de Acesso: ");
        String senha = scanner.nextLine();

        Usuario novo = new Usuario(null, nome, email, senha, LocalDateTime.now());
        
        if (usuarioDAO.salvar(novo)) {
            System.out.println("Conta criada com sucesso! Faça login para acessar.");
        } else {
            System.out.println("Falha ao registrar conta. Verifique se o e-mail já está em uso.");
        }
    }

    private static void executarLogin() {
        System.out.println("\n--- AUTENTICAÇÃO ---");
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        // Armazena o retorno na sessão caso as credenciais estejam certas
        usuarioLogado = usuarioDAO.autenticar(email, senha);

        if (usuarioLogado != null) {
            System.out.println("Autenticação realizada com sucesso!");
        } else {
            System.out.println("E-mail ou senha incorretos! Tente novamente.");
        }
    }

    // ==========================================
    // OPERAÇÕES DE CATEGORIA
    // ==========================================

    private static void criarCategoria() {
        System.out.println("\n--- NOVA CATEGORIA ---");
        System.out.print("Nome da Categoria (ex: Trabalho, Faculdade): ");
        String nomeCat = scanner.nextLine();

        Categoria novaCat = new Categoria(null, nomeCat, usuarioLogado);
        if (categoriaDAO.salvar(novaCat)) {
            System.out.println("Categoria gravada com sucesso!");
        }
    }

    private static void listarCategorias() {
        System.out.println("\n--- SUAS CATEGORIAS ---");
        List<Categoria> lista = categoriaDAO.listarPorUsuario(usuarioLogado.getId());
        
        if (lista.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
        } else {
            for (Categoria c : lista) {
                System.out.println("ID: " + c.getId() + " | Nome: " + c.getNomeCategoria());
            }
        }
    }

    // ==========================================
    // OPERAÇÕES DE TAREFA (CRUD)
    // ==========================================

    private static void criarTarefa() {
        System.out.println("\n--- NOVA TAREFA ---");
        
        // [CONDICIONAL DE PROTEÇÃO]
        // Procura as categorias do utilizador logado antes de solicitar os dados da tarefa
        List<Categoria> categoriasExistentes = categoriaDAO.listarPorUsuario(usuarioLogado.getId());
        
        if (categoriasExistentes.isEmpty()) {
            System.out.println("\n⚠️ [AVISO] Você ainda não possui categorias cadastradas!");
            System.out.println("Para criar tarefas, é necessário cadastrar pelo menos uma categoria antes (Opção 1).");
            return; // Interrompe o método imediatamente e regressa ao painel de controlo
        }

        System.out.print("Título da Tarefa: ");
        String titulo = scanner.nextLine();
        System.out.print("Descrição/Detalhes: ");
        String descricao = scanner.nextLine();
        System.out.print("Prioridade (Alta, Média, Baixa): ");
        String prioridade = scanner.nextLine();
        
        LocalDateTime prazo = null;
        while (prazo == null) {
            System.out.print("Prazo limite para conclusão (ex: 31/12/2026 18:00): ");
            String dataInput = scanner.nextLine();
            try {
                prazo = LocalDateTime.parse(dataInput, formatter);
            } catch (Exception e) {
                System.out.println("❌ Formato de data inválido! Certifique-se de usar o formato dd/MM/yyyy HH:mm");
            }
        }

        // Exibe as categorias existentes do utilizador
        System.out.println("\n--- Categorias Disponíveis ---");
        for (Categoria c : categoriasExistentes) {
            System.out.println("ID: " + c.getId() + " | Nome: " + c.getNomeCategoria());
        }
        
        // [VALIDAÇÃO DO ID INTRODUZIDO]
        int catId = -1;
        boolean idValido = false;
        
        while (!idValido) {
            System.out.print("Digite o ID da Categoria desejada (ou 0 para Nenhuma): ");
            catId = lerOpcaoInt();
            
            if (catId == 0) {
                idValido = true; // Permite tarefas sem categoria conforme a modelagem do banco
            } else {
                // Verifica se o ID inserido pertence de facto a uma das categorias da lista
                for (Categoria c : categoriasExistentes) {
                    if (c.getId() == catId) {
                        idValido = true;
                        break;
                    }
                }
                
                if (!idValido) {
                    System.out.println("❌ ID inválido! Escolha uma de suas categorias listadas ou digite 0.");
                }
            }
        }
        
        Categoria categoriaSelecionada = null;
        if (catId > 0) {
            categoriaSelecionada = new Categoria();
            categoriaSelecionada.setId(catId);
        }

        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setTitulo(titulo);
        novaTarefa.setDescricao(descricao);
        novaTarefa.setPrioridade(prioridade);
        novaTarefa.setPrazo(prazo);
        novaTarefa.setUsuario(usuarioLogado);
        novaTarefa.setCategoria(categoriaSelecionada);

        if (tarefaDAO.salvar(novaTarefa)) {
            System.out.println("Tarefa armazenada com sucesso!");
        }
    }

    private static void listarTarefas() {
        System.out.println("\n--- SUAS TAREFAS ATIVAS ---");
        List<Tarefa> lista = tarefaDAO.listarPorUsuario(usuarioLogado.getId());

        if (lista.isEmpty()) {
            System.out.println("Não há tarefas registradas para a sua conta.");
        } else {
            for (Tarefa t : lista) {
                String nomeCat = (t.getCategoria() != null) ? t.getCategoria().getNomeCategoria() : "Nenhuma";
                System.out.println("----------------------------------------");
                System.out.println("ID: " + t.getId() + " | Título: " + t.getTitulo());
                System.out.println("Status: [" + t.getStatus() + "] | Prioridade: " + t.getPrioridade());
                System.out.println("Categoria: " + nomeCat);
                System.out.println("Prazo Final: " + t.getPrazo().format(formatter));
            }
            System.out.println("----------------------------------------");
        }
    }

    private static void editarTarefa() {
        System.out.println("\n--- ATUALIZAR TAREFA ---");
        
        // Procura as tarefas do utilizador logado para validar o ID antes de prosseguir
        List<Tarefa> listaTarefas = tarefaDAO.listarPorUsuario(usuarioLogado.getId());

        if (listaTarefas.isEmpty()) {
            System.out.println("Não há tarefas registradas para a sua conta.");
            return;
        }

        // Exibe as tarefas disponíveis para facilitar a escolha do ID
        listarTarefas();
        System.out.print("Digite o ID da tarefa que deseja alterar: ");
        int idTarefa = lerOpcaoInt();

        // [VALIDAÇÃO DE EXISTÊNCIA DO ID]
        boolean idValido = false;
        for (Tarefa t : listaTarefas) {
            if (t.getId() == idTarefa) {
                idValido = true;
                break;
            }
        }

        if (!idValido) {
            System.out.println("❌ Você não possui nenhuma tarefa com o ID introduzido!");
            return; // Interrompe o método e regressa ao painel de controlo de forma segura
        }

        // Caso o ID seja válido, o sistema prossegue com as solicitações dos novos dados
        System.out.print("Novo Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Nova Descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Nova Prioridade: ");
        String prioridade = scanner.nextLine();
        
        System.out.println("Escolha o Novo Status:");
        System.out.println("1. PENDENTE | 2. CONCLUIDO | 3. EM_ATRASO");
        System.out.print("Opção de Status: ");
        int optStatus = lerOpcaoInt();
        StatusTarefa status = StatusTarefa.PENDENTE;
        if (optStatus == 2) status = StatusTarefa.CONCLUIDO;
        if (optStatus == 3) status = StatusTarefa.EM_ATRASO;

        // [VALIDAÇÃO DO FORMATO DE DATA COM TRY-CATCH]
        LocalDateTime prazo = null;
        while (prazo == null) {
            System.out.print("Novo Prazo (dd/MM/yyyy HH:mm): ");
            String dataInput = scanner.nextLine();
            try {
                prazo = LocalDateTime.parse(dataInput, formatter);
            } catch (Exception e) {
                System.out.println("❌ Formato de data inválido! Certifique-se de usar o formato dd/MM/yyyy HH:mm");
            }
        }

        Tarefa atualizada = new Tarefa();
        atualizada.setId(idTarefa);
        atualizada.setTitulo(titulo);
        atualizada.setDescricao(descricao);
        atualizada.setPrioridade(prioridade);
        atualizada.setStatus(status);
        atualizada.setPrazo(prazo);

        if (tarefaDAO.atualizar(atualizada)) {
            System.out.println("Tarefa atualizada com sucesso no Banco!");
        }
    }
    private static void eliminarTarefa() {
        System.out.println("\n--- REMOVER TAREFA ---");
        listarTarefas();
        System.out.print("Digite o ID da tarefa que deseja apagar permanentemente: ");
        int id = lerOpcaoInt();

        if (tarefaDAO.excluir(id)) {
            System.out.println("Tarefa removida com sucesso do sistema.");
        }
    }

    // ==========================================
    // MÉTODOS AUXILIARES DE SUPORTE
    // ==========================================

    /**
     * Lê uma opção inteira tratando a quebra de linha pendente no buffer do Scanner.
     * Evita o erro clássico de pular inputs de texto após ler inteiros.
     */
    private static int lerOpcaoInt() {
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer do teclado
        return opcao;
    }
}