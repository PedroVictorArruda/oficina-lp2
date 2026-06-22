package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente extends Pessoa {
    private int qtdOsConcluidas;
    private BigDecimal limiteCredito;
    private final List<Veiculo> veiculos = new ArrayList<>();

    public Cliente(int id, String nome, String cpf, String telefone, BigDecimal limiteCredito) {
        super(id, nome, cpf, telefone);
        setLimiteCredito(limiteCredito);
        this.qtdOsConcluidas = 0;
    }

    @Override
    public boolean validarCpf() {
        return algoritmoValidacaoCpf();
    }

    /** RN-04: clientes com mais de 5 OS concluídas recebem 10% de desconto. */
    public double calcularDesconto() {
        return qtdOsConcluidas > 5 ? 0.10 : 0.0;
    }

    /** Retorna true se o cliente possui crédito disponível para solicitar nova OS. */
    public boolean podeSolicitarOS() {
        return limiteCredito.compareTo(BigDecimal.ZERO) > 0;
    }

    public void registrarOsConcluida() {
        qtdOsConcluidas++;
    }

    public int getQtdOsConcluidas() { return qtdOsConcluidas; }

    public void setQtdOsConcluidas(int n) { this.qtdOsConcluidas = n; }

    public BigDecimal getLimiteCredito() { return limiteCredito; }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        if (limiteCredito == null || limiteCredito.compareTo(BigDecimal.ZERO) < 0)
            throw new DadosInvalidosException("Limite de crédito não pode ser negativo.");
        this.limiteCredito = limiteCredito;
    }

    public List<Veiculo> getVeiculos() {
        return Collections.unmodifiableList(veiculos);
    }

    public void adicionarVeiculo(Veiculo veiculo) {
        if (veiculo == null) throw new DadosInvalidosException("Veículo não pode ser nulo.");
        if (!veiculos.contains(veiculo)) veiculos.add(veiculo);
    }

    @Override
    public String toString() {
        return "Cliente{" + super.toString() +
                ", osConc=" + qtdOsConcluidas +
                ", crédito=R$ " + limiteCredito +
                ", veículos=" + veiculos.size() + "}";
    }
}
