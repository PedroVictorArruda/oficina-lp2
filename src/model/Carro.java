package model;

import java.math.BigDecimal;

public class Carro extends Veiculo {
    private static final BigDecimal CUSTO_BASE = new BigDecimal("100.00");

    private int nrPortas;
    private TipoCombustivel tipoCombustivel;

    public Carro(int id, String placa, String modelo, int ano, Cliente dono,
                 int nrPortas, TipoCombustivel tipoCombustivel) {
        super(id, placa, modelo, ano, dono);
        setNrPortas(nrPortas);
        setTipoCombustivel(tipoCombustivel);
    }

    /** Carros flex recebem adicional de 5% no custo base. */
    @Override
    public BigDecimal calcularCustoBase() {
        if (tipoCombustivel == TipoCombustivel.FLEX) {
            return CUSTO_BASE.multiply(new BigDecimal("1.05"));
        }
        return CUSTO_BASE;
    }

    public int getNrPortas() { return nrPortas; }

    public TipoCombustivel getTipoCombustivel() { return tipoCombustivel; }

    public void setNrPortas(int nrPortas) {
        if (nrPortas < 2 || nrPortas > 5)
            throw new DadosInvalidosException("Número de portas deve ser entre 2 e 5.");
        this.nrPortas = nrPortas;
    }

    public void setTipoCombustivel(TipoCombustivel tipoCombustivel) {
        if (tipoCombustivel == null) throw new DadosInvalidosException("Tipo de combustível não pode ser nulo.");
        this.tipoCombustivel = tipoCombustivel;
    }

    @Override
    public String toString() {
        return "Carro{" + super.toString() +
                ", portas=" + nrPortas +
                ", combustível=" + tipoCombustivel +
                ", custoBase=R$ " + calcularCustoBase() + "}";
    }
}
