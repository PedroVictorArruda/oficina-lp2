package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdemServico {
    private final int id;
    private final Cliente cliente;
    private final Veiculo veiculo;
    private Mecanico mecanico;
    private StatusOrdem status;
    private final LocalDate data;
    private final List<Servico> servicos = new ArrayList<>();

    /** Construtor de carregamento — restaura uma OS a partir dos dados persistidos. */
    public OrdemServico(int id, Cliente cliente, Veiculo veiculo,
                        Mecanico mecanico, StatusOrdem status, java.time.LocalDate data) {
        if (cliente == null) throw new IllegalArgumentException("Cliente é obrigatório.");
        if (veiculo == null) throw new IllegalArgumentException("Veículo é obrigatório.");
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.mecanico = mecanico;
        this.status = status;
        this.data = data;
    }

    /** Adiciona serviço durante carregamento (sem validar status da OS). */
    public void adicionarServicoCarregado(Servico servico) {
        if (servico != null) servicos.add(servico);
    }

    /** Cria uma OS em estado ABERTA. Mecânico é atribuído posteriormente ao avançar estado. */
    public OrdemServico(int id, Cliente cliente, Veiculo veiculo) {
        if (cliente == null) throw new IllegalArgumentException("Cliente é obrigatório.");
        if (veiculo == null) throw new IllegalArgumentException("Veículo é obrigatório.");
        if (veiculo.getDono().getId() != cliente.getId())
            throw new IllegalArgumentException("O veículo informado não pertence ao cliente.");
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.status = StatusOrdem.ABERTA;
        this.data = LocalDate.now();
    }

    /**
     * Avança a OS para o próximo estado conforme a máquina de estados:
     * ABERTA → DIAGNOSTICO (requer mecânico atribuído)
     * DIAGNOSTICO → APROVADA (cliente aprova o orçamento)
     * APROVADA → EM_EXECUCAO (verificação de peças feita pelo OficinaService antes desta chamada)
     * EM_EXECUCAO → CONCLUIDA (requer pelo menos um serviço)
     */
    public void avancarEstado() {
        if (status == StatusOrdem.ABERTA) {
            if (mecanico == null)
                throw new TransicaoEstadoInvalidaException(
                        "É necessário atribuir um mecânico antes de avançar para DIAGNOSTICO.");
            status = StatusOrdem.DIAGNOSTICO;
        } else if (status == StatusOrdem.DIAGNOSTICO) {
            status = StatusOrdem.APROVADA;
        } else if (status == StatusOrdem.APROVADA) {
            status = StatusOrdem.EM_EXECUCAO;
        } else if (status == StatusOrdem.EM_EXECUCAO) {
            if (servicos.isEmpty())
                throw new TransicaoEstadoInvalidaException(
                        "Não é possível concluir uma OS sem serviços.");
            status = StatusOrdem.CONCLUIDA;
            cliente.registrarOsConcluida();
        } else {
            throw new TransicaoEstadoInvalidaException(
                    "Estado " + status + " não permite avanço.");
        }
    }

    /**
     * Cancela a OS. Só é permitido nos estados ABERTA, DIAGNOSTICO e APROVADA.
     * CANCELADA é estado terminal — não pode ser reativada.
     */
    public void cancelar() {
        if (status == StatusOrdem.EM_EXECUCAO)
            throw new TransicaoEstadoInvalidaException(
                    "Não é possível cancelar uma OS já em execução.");
        if (status == StatusOrdem.CONCLUIDA || status == StatusOrdem.CANCELADA)
            throw new TransicaoEstadoInvalidaException(
                    "OS já finalizada (status: " + status + ").");
        status = StatusOrdem.CANCELADA;
    }

    public void atribuirMecanico(Mecanico mecanico) {
        if (mecanico == null) throw new IllegalArgumentException("Mecânico não pode ser nulo.");
        if (status != StatusOrdem.ABERTA)
            throw new TransicaoEstadoInvalidaException(
                    "Mecânico só pode ser atribuído enquanto a OS estiver ABERTA.");
        this.mecanico = mecanico;
    }

    public void adicionarServico(Servico servico) {
        if (status == StatusOrdem.CONCLUIDA || status == StatusOrdem.CANCELADA)
            throw new IllegalStateException("Não é possível adicionar serviço a uma OS finalizada.");
        if (servico == null) throw new IllegalArgumentException("Serviço não pode ser nulo.");
        servicos.add(servico);
    }

    /** Calcula o total da OS: custo base do veículo + serviços (com peças) — aplicando desconto de fidelidade. */
    public BigDecimal calcularTotal() {
        BigDecimal total = veiculo.calcularCustoBase();
        for (Servico servico : servicos) {
            total = total.add(servico.calcularTotal());
        }
        double desconto = cliente.calcularDesconto();
        if (desconto > 0) {
            total = total.subtract(total.multiply(new BigDecimal(desconto)));
        }
        return total;
    }

    public int getId() { return id; }

    public Cliente getCliente() { return cliente; }

    public Veiculo getVeiculo() { return veiculo; }

    public Mecanico getMecanico() { return mecanico; }

    public StatusOrdem getStatus() { return status; }

    public LocalDate getData() { return data; }

    public List<Servico> getServicos() { return Collections.unmodifiableList(servicos); }

    @Override
    public String toString() {
        return "OrdemServico{id=" + id +
                ", cliente='" + cliente.getNome() + "'" +
                ", veículo='" + veiculo.getPlaca() + "'" +
                ", mecânico=" + (mecanico != null ? "'" + mecanico.getNome() + "'" : "não atribuído") +
                ", status=" + status +
                ", data=" + data +
                ", total=R$ " + calcularTotal() + "}";
    }
}
