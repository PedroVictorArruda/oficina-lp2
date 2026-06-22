package model;

import java.math.BigDecimal;

public class Moto extends Veiculo {
    private static final BigDecimal CUSTO_BASE = new BigDecimal("80.00");
    private static final int LIMITE_CC_ALTO = 600;

    private int cilindrada;
    private TipoMoto tipoMoto;

    public Moto(int id, String placa, String modelo, int ano, Cliente dono,
                int cilindrada, TipoMoto tipoMoto) {
        super(id, placa, modelo, ano, dono);
        setCilindrada(cilindrada);
        setTipoMoto(tipoMoto);
    }

    /** Motos acima de 600cc recebem adicional de 15% no custo base. */
    @Override
    public BigDecimal calcularCustoBase() {
        if (cilindrada > LIMITE_CC_ALTO) {
            return CUSTO_BASE.multiply(new BigDecimal("1.15"));
        }
        return CUSTO_BASE;
    }

    public int getCilindrada() { return cilindrada; }

    public TipoMoto getTipoMoto() { return tipoMoto; }

    public void setCilindrada(int cilindrada) {
        if (cilindrada <= 0)
            throw new DadosInvalidosException("Cilindrada deve ser maior que zero.");
        this.cilindrada = cilindrada;
    }

    public void setTipoMoto(TipoMoto tipoMoto) {
        if (tipoMoto == null) throw new DadosInvalidosException("Tipo de moto não pode ser nulo.");
        this.tipoMoto = tipoMoto;
    }

    @Override
    public String toString() {
        return "Moto{" + super.toString() +
                ", cilindrada=" + cilindrada + "cc" +
                ", tipo=" + tipoMoto +
                ", custoBase=R$ " + calcularCustoBase() + "}";
    }
}
