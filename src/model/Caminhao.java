package model;

import java.math.BigDecimal;

public class Caminhao extends Veiculo {
    private static final BigDecimal CUSTO_BASE = new BigDecimal("200.00");
    private static final double LIMITE_CARGA_PESADA = 5.0;

    private double capCarga;
    private int nrEixos;

    public Caminhao(int id, String placa, String modelo, int ano, Cliente dono,
                    double capCarga, int nrEixos) {
        super(id, placa, modelo, ano, dono);
        setCapCarga(capCarga);
        setNrEixos(nrEixos);
    }

    /** Caminhões com capacidade acima de 5t recebem adicional de 20% no custo base. */
    @Override
    public BigDecimal calcularCustoBase() {
        if (capCarga > LIMITE_CARGA_PESADA) {
            return CUSTO_BASE.multiply(new BigDecimal("1.20"));
        }
        return CUSTO_BASE;
    }

    public double getCapCarga() { return capCarga; }

    public int getNrEixos() { return nrEixos; }

    public void setCapCarga(double capCarga) {
        if (capCarga <= 0)
            throw new DadosInvalidosException("Capacidade de carga deve ser maior que zero.");
        this.capCarga = capCarga;
    }

    public void setNrEixos(int nrEixos) {
        if (nrEixos < 2)
            throw new DadosInvalidosException("Número de eixos deve ser pelo menos 2.");
        this.nrEixos = nrEixos;
    }

    @Override
    public String toString() {
        return "Caminhao{" + super.toString() +
                ", capCarga=" + capCarga + "t" +
                ", eixos=" + nrEixos +
                ", custoBase=R$ " + calcularCustoBase() + "}";
    }
}
