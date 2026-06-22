package app;

import model.*;
import service.OficinaService;
import util.ConsoleUtils;

public class Main {
    private static final OficinaService oficina = new OficinaService();
    private static final ConsoleUtils console = new ConsoleUtils();

    public static void main(String[] args) {
        int opcao;
        do {
            limparTela();
            mostrarMenu();
            opcao = console.lerInteiro("Escolha uma opção: ");
            try {
                executarOpcao(opcao);
            } catch (TransicaoEstadoInvalidaException e) {
                System.out.println("ERRO DE TRANSIÇÃO: " + e.getMessage());
            } catch (EstoqueInsuficienteException e) {
                System.out.println("ERRO DE ESTOQUE: " + e.getMessage());
            } catch (MecanicoIncompativelException e) {
                System.out.println("ERRO DE MECÂNICO: " + e.getMessage());
            } catch (VeiculoBloqueadoException e) {
                System.out.println("ERRO DE VEÍCULO: " + e.getMessage());
            } catch (DadosInvalidosException e) {
                System.out.println("DADOS INVÁLIDOS: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("ERRO: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("ERRO: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
            if (opcao != 0) pausar();
        } while (opcao != 0);
        System.out.println("Sistema encerrado.");
    }

    private static void mostrarMenu() {
        System.out.println(
            "======== OFICINA MECÂNICA ========\n" +
            "--- Cadastros ---\n" +
            "1  - Cadastrar cliente\n" +
            "2  - Cadastrar mecânico\n" +
            "3  - Cadastrar carro\n" +
            "4  - Cadastrar moto\n" +
            "5  - Cadastrar caminhão\n" +
            "6  - Cadastrar peça\n" +
            "7  - Cadastrar serviço preventivo\n" +
            "8  - Cadastrar serviço corretivo\n" +
            "--- Listagens ---\n" +
            "9  - Listar clientes\n" +
            "10 - Listar mecânicos\n" +
            "11 - Listar veículos\n" +
            "12 - Listar peças / alertas de estoque\n" +
            "13 - Listar serviços\n" +
            "14 - Listar ordens de serviço\n" +
            "--- Ordens de Serviço ---\n" +
            "15 - Abrir ordem de serviço\n" +
            "16 - Atribuir mecânico à OS\n" +
            "17 - Adicionar serviço à OS\n" +
            "18 - Adicionar peça a serviço\n" +
            "19 - Avançar estado da OS\n" +
            "20 - Cancelar OS\n" +
            "0  - Sair"
        );
    }

    private static void executarOpcao(int opcao) {
        switch (opcao) {
            case 1:  cadastrarCliente(); break;
            case 2:  cadastrarMecanico(); break;
            case 3:  cadastrarCarro(); break;
            case 4:  cadastrarMoto(); break;
            case 5:  cadastrarCaminhao(); break;
            case 6:  cadastrarPeca(); break;
            case 7:  cadastrarServicoPreventivo(); break;
            case 8:  cadastrarServicoCorretivo(); break;
            case 9:  listar("Clientes", oficina.listarClientes()); break;
            case 10: listar("Mecânicos", oficina.listarMecanicos()); break;
            case 11: listar("Veículos", oficina.listarVeiculos()); break;
            case 12: listar("Peças", oficina.listarPecas()); oficina.verificarAlertasEstoque(); break;
            case 13: listar("Serviços", oficina.listarServicos()); break;
            case 14: listar("Ordens de Serviço", oficina.listarOrdens()); break;
            case 15: abrirOrdem(); break;
            case 16: atribuirMecanico(); break;
            case 17: adicionarServicoNaOrdem(); break;
            case 18: adicionarPecaAoServico(); break;
            case 19: avancarEstado(); break;
            case 20: cancelarOrdem(); break;
            case 0:  break;
            default: System.out.println("Opção inválida.");
        }
    }

    // ───────────────────────────── CADASTROS ─────────────────────────────

    private static void cadastrarCliente() {
        String nome = console.lerTexto("Nome: ");
        String cpf  = console.lerTexto("CPF (somente 11 dígitos): ");
        String tel  = console.lerTexto("Telefone (10 ou 11 dígitos): ");
        java.math.BigDecimal limite = console.lerDecimal("Limite de crédito (R$): ");
        System.out.println("Cadastrado: " + oficina.cadastrarCliente(nome, cpf, tel, limite));
    }

    private static void cadastrarMecanico() {
        String nome = console.lerTexto("Nome: ");
        String cpf  = console.lerTexto("CPF (somente 11 dígitos): ");
        String tel  = console.lerTexto("Telefone: ");
        Especialidade esp = escolherEspecialidade();
        java.math.BigDecimal salario = console.lerDecimal("Salário (R$): ");
        System.out.println("Cadastrado: " + oficina.cadastrarMecanico(nome, cpf, tel, esp, salario));
    }

    private static void cadastrarCarro() {
        listar("Clientes", oficina.listarClientes());
        int clienteId = console.lerInteiro("ID do cliente: ");
        String placa  = console.lerTexto("Placa: ");
        String modelo = console.lerTexto("Modelo: ");
        int ano       = console.lerInteiro("Ano: ");
        int portas    = console.lerInteiro("Nº de portas (2-5): ");
        TipoCombustivel comb = escolherTipoCombustivel();
        System.out.println("Cadastrado: " + oficina.cadastrarCarro(placa, modelo, ano, clienteId, portas, comb));
    }

    private static void cadastrarMoto() {
        listar("Clientes", oficina.listarClientes());
        int clienteId = console.lerInteiro("ID do cliente: ");
        String placa  = console.lerTexto("Placa: ");
        String modelo = console.lerTexto("Modelo: ");
        int ano       = console.lerInteiro("Ano: ");
        int cc        = console.lerInteiro("Cilindrada (cc): ");
        TipoMoto tipo = escolherTipoMoto();
        System.out.println("Cadastrada: " + oficina.cadastrarMoto(placa, modelo, ano, clienteId, cc, tipo));
    }

    private static void cadastrarCaminhao() {
        listar("Clientes", oficina.listarClientes());
        int clienteId = console.lerInteiro("ID do cliente: ");
        String placa  = console.lerTexto("Placa: ");
        String modelo = console.lerTexto("Modelo: ");
        int ano       = console.lerInteiro("Ano: ");
        double carga  = console.lerDecimal("Capacidade de carga (t): ").doubleValue();
        int eixos     = console.lerInteiro("Nº de eixos: ");
        System.out.println("Cadastrado: " + oficina.cadastrarCaminhao(placa, modelo, ano, clienteId, carga, eixos));
    }

    private static void cadastrarPeca() {
        String codigo = console.lerTexto("Código (ex: FIL-001): ");
        String nome   = console.lerTexto("Nome da peça: ");
        java.math.BigDecimal preco = console.lerDecimal("Preço (R$): ");
        int estoque   = console.lerInteiro("Estoque inicial: ");
        System.out.println("Cadastrada: " + oficina.cadastrarPeca(codigo, nome, preco, estoque));
    }

    private static void cadastrarServicoPreventivo() {
        String desc   = console.lerTexto("Descrição: ");
        java.math.BigDecimal preco = console.lerDecimal("Preço base (R$): ");
        int intervalo = console.lerInteiro("Intervalo de revisão (km): ");
        int ultimaRev = console.lerInteiro("Km da última revisão: ");
        int kmAtual   = console.lerInteiro("Km atual do veículo: ");
        System.out.println("Cadastrado: " + oficina.cadastrarServicoPreventivo(desc, preco, intervalo, ultimaRev, kmAtual));
    }

    private static void cadastrarServicoCorretivo() {
        String desc   = console.lerTexto("Descrição: ");
        java.math.BigDecimal preco = console.lerDecimal("Preço base (R$): ");
        NivelUrgencia urg = escolherNivelUrgencia();
        Especialidade cat = escolherEspecialidade();
        System.out.println("Cadastrado: " + oficina.cadastrarServicoCorretivo(desc, preco, urg, cat));
    }

    // ───────────────────────────── ORDENS ─────────────────────────────

    private static void abrirOrdem() {
        listar("Clientes", oficina.listarClientes());
        int clienteId = console.lerInteiro("ID do cliente: ");
        listar("Veículos", oficina.listarVeiculos());
        int veiculoId = console.lerInteiro("ID do veículo: ");
        OrdemServico os = oficina.abrirOrdemServico(clienteId, veiculoId);
        System.out.println("Ordem aberta: " + os);
        System.out.println("Dica: atribua um mecânico (opção 16) e avance o estado (opção 19).");
    }

    private static void atribuirMecanico() {
        listar("Ordens", oficina.listarOrdens());
        int ordemId    = console.lerInteiro("ID da OS: ");
        listar("Mecânicos", oficina.listarMecanicos());
        int mecanicoId = console.lerInteiro("ID do mecânico: ");
        oficina.atribuirMecanico(ordemId, mecanicoId);
        System.out.println("Mecânico atribuído com sucesso.");
    }

    private static void adicionarServicoNaOrdem() {
        listar("Ordens", oficina.listarOrdens());
        int ordemId   = console.lerInteiro("ID da OS: ");
        listar("Serviços", oficina.listarServicos());
        int servicoId = console.lerInteiro("ID do serviço: ");
        oficina.adicionarServicoNaOrdem(ordemId, servicoId);
        System.out.println("Serviço adicionado com sucesso.");
    }

    private static void adicionarPecaAoServico() {
        listar("Serviços", oficina.listarServicos());
        int servicoId = console.lerInteiro("ID do serviço: ");
        listar("Peças em estoque", oficina.listarPecas());
        int pecaId    = console.lerInteiro("ID da peça: ");
        oficina.adicionarPecaNoServico(servicoId, pecaId);
        System.out.println("Peça adicionada ao serviço.");
    }

    private static void avancarEstado() {
        listar("Ordens", oficina.listarOrdens());
        int ordemId = console.lerInteiro("ID da OS: ");
        oficina.avancarEstadoOrdem(ordemId);
        OrdemServico ordem = oficina.buscarOrdemPorId(ordemId);
        System.out.println("Estado avançado para: " + ordem.getStatus());
    }

    private static void cancelarOrdem() {
        listar("Ordens", oficina.listarOrdens());
        int ordemId = console.lerInteiro("ID da OS: ");
        oficina.cancelarOrdem(ordemId);
        System.out.println("OS cancelada com sucesso.");
    }

    // ───────────────────────────── SELETORES DE ENUM ─────────────────────────────

    private static Especialidade escolherEspecialidade() {
        System.out.println("Especialidades: 1-MOTOR  2-ELETRICA  3-FUNILARIA  4-SUSPENSAO  5-GERAL");
        int op = console.lerInteiro("Especialidade: ");
        switch (op) {
            case 1:  return Especialidade.MOTOR;
            case 2:  return Especialidade.ELETRICA;
            case 3:  return Especialidade.FUNILARIA;
            case 4:  return Especialidade.SUSPENSAO;
            default: return Especialidade.GERAL;
        }
    }

    private static TipoCombustivel escolherTipoCombustivel() {
        System.out.println("Combustível: 1-GASOLINA  2-ALCOOL  3-FLEX  4-DIESEL  5-ELETRICO");
        int op = console.lerInteiro("Combustível: ");
        switch (op) {
            case 1:  return TipoCombustivel.GASOLINA;
            case 2:  return TipoCombustivel.ALCOOL;
            case 3:  return TipoCombustivel.FLEX;
            case 4:  return TipoCombustivel.DIESEL;
            default: return TipoCombustivel.ELETRICO;
        }
    }

    private static TipoMoto escolherTipoMoto() {
        System.out.println("Tipo: 1-ESPORTIVA  2-TOURING  3-NAKED  4-SCOOTER  5-TRAIL");
        int op = console.lerInteiro("Tipo: ");
        switch (op) {
            case 1:  return TipoMoto.ESPORTIVA;
            case 2:  return TipoMoto.TOURING;
            case 3:  return TipoMoto.NAKED;
            case 4:  return TipoMoto.SCOOTER;
            default: return TipoMoto.TRAIL;
        }
    }

    private static NivelUrgencia escolherNivelUrgencia() {
        System.out.println("Urgência: 1-BAIXA  2-MEDIA  3-ALTA");
        int op = console.lerInteiro("Urgência: ");
        switch (op) {
            case 1:  return NivelUrgencia.BAIXA;
            case 3:  return NivelUrgencia.ALTA;
            default: return NivelUrgencia.MEDIA;
        }
    }

    // ───────────────────────────── UTILITÁRIOS ─────────────────────────────

    private static void listar(String titulo, java.util.List<?> itens) {
        System.out.println("----- " + titulo + " -----");
        if (itens.isEmpty()) { System.out.println("Nenhum registro."); return; }
        for (Object item : itens) System.out.println(item);
    }

    private static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        console.aguardar();
    }
}
