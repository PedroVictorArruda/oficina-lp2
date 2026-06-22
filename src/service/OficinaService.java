package service;

import model.*;
import persistencia.GerenciadorPersistencia;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class OficinaService {
    private final java.util.List<Cliente> clientes = new java.util.ArrayList<>();
    private final java.util.List<Mecanico> mecanicos = new java.util.ArrayList<>();
    private final java.util.List<Veiculo> veiculos = new java.util.ArrayList<>();
    private final Estoque estoque = new Estoque(2);
    private final java.util.List<Servico> servicos = new java.util.ArrayList<>();
    private final java.util.List<OrdemServico> ordens = new java.util.ArrayList<>();

    private int proximoClienteId = 1;
    private int proximoMecanicoId = 1;
    private int proximoVeiculoId = 1;
    private int proximoPecaId = 1;
    private int proximoServicoId = 1;
    private int proximoOrdemId = 1;

    public OficinaService() {
        GerenciadorPersistencia.carregarTudo(this);
    }

    // ───────────────────────────── CADASTROS ─────────────────────────────

    public Cliente cadastrarCliente(String nome, String cpf, String telefone, BigDecimal limiteCredito) {
        if (clientes.stream().anyMatch(c -> c.getNome().equalsIgnoreCase(nome.trim())))
            throw new DadosInvalidosException("Já existe cliente com esse nome.");
        if (clientes.stream().anyMatch(c -> c.getTelefone().equals(telefone.trim())))
            throw new DadosInvalidosException("Já existe cliente com esse telefone.");
        Cliente cliente = new Cliente(proximoClienteId++, nome, cpf, telefone, limiteCredito);
        if (!cliente.validarCpf())
            throw new DadosInvalidosException("CPF inválido para o cliente.");
        clientes.add(cliente);
        GerenciadorPersistencia.salvarTudo(this);
        return cliente;
    }

    public Mecanico cadastrarMecanico(String nome, String cpf, String telefone,
                                       Especialidade especialidade, BigDecimal salario) {
        if (mecanicos.stream().anyMatch(m -> m.getNome().equalsIgnoreCase(nome.trim())))
            throw new DadosInvalidosException("Já existe mecânico com esse nome.");
        Mecanico mecanico = new Mecanico(proximoMecanicoId++, nome, cpf, telefone, especialidade, salario);
        if (!mecanico.validarCpf())
            throw new DadosInvalidosException("CPF inválido para o mecânico.");
        mecanicos.add(mecanico);
        GerenciadorPersistencia.salvarTudo(this);
        return mecanico;
    }

    public Carro cadastrarCarro(String placa, String modelo, int ano, int clienteId,
                                 int nrPortas, TipoCombustivel combustivel) {
        if (veiculos.stream().anyMatch(v -> v.getPlaca().equalsIgnoreCase(placa.trim())))
            throw new DadosInvalidosException("Já existe veículo com essa placa.");
        Cliente cliente = buscarClientePorId(clienteId);
        Carro carro = new Carro(proximoVeiculoId++, placa, modelo, ano, cliente, nrPortas, combustivel);
        veiculos.add(carro);
        cliente.adicionarVeiculo(carro);
        GerenciadorPersistencia.salvarTudo(this);
        return carro;
    }

    public Moto cadastrarMoto(String placa, String modelo, int ano, int clienteId,
                               int cilindrada, TipoMoto tipoMoto) {
        if (veiculos.stream().anyMatch(v -> v.getPlaca().equalsIgnoreCase(placa.trim())))
            throw new DadosInvalidosException("Já existe veículo com essa placa.");
        Cliente cliente = buscarClientePorId(clienteId);
        Moto moto = new Moto(proximoVeiculoId++, placa, modelo, ano, cliente, cilindrada, tipoMoto);
        veiculos.add(moto);
        cliente.adicionarVeiculo(moto);
        GerenciadorPersistencia.salvarTudo(this);
        return moto;
    }

    public Caminhao cadastrarCaminhao(String placa, String modelo, int ano, int clienteId,
                                       double capCarga, int nrEixos) {
        if (veiculos.stream().anyMatch(v -> v.getPlaca().equalsIgnoreCase(placa.trim())))
            throw new DadosInvalidosException("Já existe veículo com essa placa.");
        Cliente cliente = buscarClientePorId(clienteId);
        Caminhao caminhao = new Caminhao(proximoVeiculoId++, placa, modelo, ano, cliente, capCarga, nrEixos);
        veiculos.add(caminhao);
        cliente.adicionarVeiculo(caminhao);
        GerenciadorPersistencia.salvarTudo(this);
        return caminhao;
    }

    public Peca cadastrarPeca(String codigo, String nome, BigDecimal preco, int qtdEstoque) {
        if (estoque.getListaPecas().stream().anyMatch(p -> p.getNome().equalsIgnoreCase(nome.trim())))
            throw new DadosInvalidosException("Já existe peça com esse nome.");
        Peca peca = new Peca(proximoPecaId++, codigo, nome, preco, qtdEstoque);
        estoque.adicionarPeca(peca);
        GerenciadorPersistencia.salvarTudo(this);
        return peca;
    }

    public ServicoPreventivo cadastrarServicoPreventivo(String descricao, BigDecimal precoBase,
                                                         int intervaloKm, int ultimaRevisaoKm, int kmAtual) {
        if (servicos.stream().anyMatch(s -> s.getDescricao().equalsIgnoreCase(descricao.trim())))
            throw new DadosInvalidosException("Já existe serviço com essa descrição.");
        ServicoPreventivo sp = new ServicoPreventivo(
                proximoServicoId++, descricao, precoBase, intervaloKm, ultimaRevisaoKm, kmAtual);
        servicos.add(sp);
        GerenciadorPersistencia.salvarTudo(this);
        return sp;
    }

    public ServicoCorretivo cadastrarServicoCorretivo(String descricao, BigDecimal precoBase,
                                                       NivelUrgencia urgencia, Especialidade categoria) {
        if (servicos.stream().anyMatch(s -> s.getDescricao().equalsIgnoreCase(descricao.trim())))
            throw new DadosInvalidosException("Já existe serviço com essa descrição.");
        ServicoCorretivo sc = new ServicoCorretivo(
                proximoServicoId++, descricao, precoBase, urgencia, categoria);
        servicos.add(sc);
        GerenciadorPersistencia.salvarTudo(this);
        return sc;
    }

    // ───────────────────────────── ORDENS DE SERVIÇO ─────────────────────────────

    public OrdemServico abrirOrdemServico(int clienteId, int veiculoId) {
        Cliente cliente = buscarClientePorId(clienteId);

        // RN-04 check: cliente pode solicitar OS?
        if (!cliente.podeSolicitarOS())
            throw new DadosInvalidosException("Cliente sem crédito disponível para nova OS.");

        Veiculo veiculo = buscarVeiculoPorId(veiculoId);

        // RN-03: verifica se o veículo está bloqueado por OS em execução com ServicoCorretivo ALTA
        for (OrdemServico os : ordens) {
            if (os.getVeiculo().getId() == veiculoId && os.getStatus() == StatusOrdem.EM_EXECUCAO) {
                for (Servico s : os.getServicos()) {
                    if (s instanceof ServicoCorretivo sc && sc.bloquearVeiculo()) {
                        throw new VeiculoBloqueadoException(
                                "Veículo bloqueado: possui OS em execução com ServicoCorretivo de urgência ALTA.");
                    }
                }
            }
        }

        OrdemServico ordem = new OrdemServico(proximoOrdemId++, cliente, veiculo);
        ordens.add(ordem);
        GerenciadorPersistencia.salvarTudo(this);
        return ordem;
    }

    public void atribuirMecanico(int ordemId, int mecanicoId) {
        OrdemServico ordem = buscarOrdemPorId(ordemId);
        Mecanico mecanico = buscarMecanicoPorId(mecanicoId);
        ordem.atribuirMecanico(mecanico);
        GerenciadorPersistencia.salvarTudo(this);
    }

    public void adicionarServicoNaOrdem(int ordemId, int servicoId) {
        OrdemServico ordem = buscarOrdemPorId(ordemId);
        Servico servico = buscarServicoPorId(servicoId);

        // RN-01: ServicoCorretivo de categoria MOTOR exige mecânico com especialidade MOTOR
        if (servico instanceof ServicoCorretivo sc && ordem.getMecanico() != null) {
            if (!ordem.getMecanico().podeExecutar(sc.getCategoria())) {
                throw new MecanicoIncompativelException(
                        "Mecânico '" + ordem.getMecanico().getNome() +
                        "' (especialidade: " + ordem.getMecanico().getEspecialidade() +
                        ") não pode executar serviço de categoria " + sc.getCategoria() + ".");
            }
        }

        ordem.adicionarServico(servico);
        GerenciadorPersistencia.salvarTudo(this);
    }

    public void adicionarPecaNoServico(int servicoId, int pecaId) {
        Servico servico = buscarServicoPorId(servicoId);
        Peca peca = estoque.buscarPorId(pecaId);
        if (!estoque.verificarDisponibilidade(peca, 1))
            throw new EstoqueInsuficienteException("Peça '" + peca.getNome() + "' sem estoque.");
        peca.reservar(1);
        estoque.alertarReposicao();
        servico.adicionarPeca(peca);
        GerenciadorPersistencia.salvarTudo(this);
    }

    public void avancarEstadoOrdem(int ordemId) {
        OrdemServico ordem = buscarOrdemPorId(ordemId);

        // RN-02: ao avançar de APROVADA para EM_EXECUCAO, verifica disponibilidade de peças
        if (ordem.getStatus() == StatusOrdem.APROVADA) {
            for (Servico servico : ordem.getServicos()) {
                for (Peca peca : servico.getPecas()) {
                    if (!estoque.verificarDisponibilidade(peca, 1)) {
                        throw new EstoqueInsuficienteException(
                                "Peça '" + peca.getNome() + "' sem estoque suficiente para iniciar a OS.");
                    }
                }
            }
        }

        ordem.avancarEstado();
        GerenciadorPersistencia.salvarTudo(this);
    }

    public void cancelarOrdem(int ordemId) {
        buscarOrdemPorId(ordemId).cancelar();
        GerenciadorPersistencia.salvarTudo(this);
    }

    public void verificarAlertasEstoque() {
        estoque.alertarReposicao();
    }

    // ───────────────────────────── LISTAGENS ─────────────────────────────

    public List<Cliente> listarClientes() { return Collections.unmodifiableList(clientes); }

    public List<Mecanico> listarMecanicos() { return Collections.unmodifiableList(mecanicos); }

    public List<Veiculo> listarVeiculos() { return Collections.unmodifiableList(veiculos); }

    public List<Peca> listarPecas() { return estoque.getListaPecas(); }

    public List<Servico> listarServicos() { return Collections.unmodifiableList(servicos); }

    public List<OrdemServico> listarOrdens() { return Collections.unmodifiableList(ordens); }

    // ───────────────────────────── BUSCAS ─────────────────────────────

    public Cliente buscarClientePorId(int id) {
        return clientes.stream().filter(c -> c.getId() == id).findFirst()
                .orElseThrow(() -> new DadosInvalidosException("Cliente não encontrado (id=" + id + ")."));
    }

    public Mecanico buscarMecanicoPorId(int id) {
        return mecanicos.stream().filter(m -> m.getId() == id).findFirst()
                .orElseThrow(() -> new DadosInvalidosException("Mecânico não encontrado (id=" + id + ")."));
    }

    public Veiculo buscarVeiculoPorId(int id) {
        return veiculos.stream().filter(v -> v.getId() == id).findFirst()
                .orElseThrow(() -> new DadosInvalidosException("Veículo não encontrado (id=" + id + ")."));
    }

    public Servico buscarServicoPorId(int id) {
        return servicos.stream().filter(s -> s.getId() == id).findFirst()
                .orElseThrow(() -> new DadosInvalidosException("Serviço não encontrado (id=" + id + ")."));
    }

    public OrdemServico buscarOrdemPorId(int id) {
        return ordens.stream().filter(o -> o.getId() == id).findFirst()
                .orElseThrow(() -> new DadosInvalidosException("Ordem não encontrada (id=" + id + ")."));
    }

    public Peca buscarPecaPorId(int id) {
        return estoque.getListaPecas().stream().filter(p -> p.getId() == id).findFirst()
                .orElseThrow(() -> new DadosInvalidosException("Peça não encontrada (id=" + id + ")."));
    }

    // ───────────────────────────── CARREGAMENTO (uso exclusivo da persistência) ─────────────────────────────

    public void adicionarClienteCarregado(Cliente c) {
        clientes.add(c);
        if (c.getId() >= proximoClienteId) proximoClienteId = c.getId() + 1;
    }

    public void adicionarMecanicoCarregado(Mecanico m) {
        mecanicos.add(m);
        if (m.getId() >= proximoMecanicoId) proximoMecanicoId = m.getId() + 1;
    }

    public void adicionarPecaCarregada(Peca p) {
        estoque.adicionarPeca(p);
        if (p.getId() >= proximoPecaId) proximoPecaId = p.getId() + 1;
    }

    public void adicionarVeiculoCarregado(Veiculo v) {
        veiculos.add(v);
        v.getDono().adicionarVeiculo(v);
        if (v.getId() >= proximoVeiculoId) proximoVeiculoId = v.getId() + 1;
    }

    public void adicionarServicoCarregado(Servico s) {
        servicos.add(s);
        if (s.getId() >= proximoServicoId) proximoServicoId = s.getId() + 1;
    }

    public void adicionarOrdemCarregada(OrdemServico o) {
        ordens.add(o);
        if (o.getId() >= proximoOrdemId) proximoOrdemId = o.getId() + 1;
    }
}
