package model;

import java.math.BigDecimal;

public class ServicoPreventivo extends Servico {
    private int intervaloKm;
    private int ultimaRevisaoKm;
    private int kmAtual;

    public ServicoPreventivo(int id, String descricao, BigDecimal precoBase,
                              int intervaloKm, int ultimaRevisaoKm, int kmAtual) {
        super(id, descricao, precoBase);
        setIntervaloKm(intervaloKm);
        setUltimaRevisaoKm(ultimaRevisaoKm);
        setKmAtual(kmAtual);
    }

    /**
     * RN-05: revisão vencida (km atual acima do próximo intervalo recomendado)
     * aplica acréscimo de 20%; caso contrário, aplica o preço base normalmente.
     */
    @Override
    public BigDecimal calcularCusto() {
        if (estaVencido()) {
            return getPrecoBase().multiply(new BigDecimal("1.20"));
        }
        return getPrecoBase();
    }

    /** Retorna true se o veículo ultrapassou o km recomendado para revisão. */
    public boolean estaVencido() {
        return kmAtual > ultimaRevisaoKm + intervaloKm;
    }

    public int getIntervaloKm() { return intervaloKm; }

    public int getUltimaRevisaoKm() { return ultimaRevisaoKm; }

    public int getKmAtual() { return kmAtual; }

    public void setIntervaloKm(int intervaloKm) {
        if (intervaloKm <= 0) throw new DadosInvalidosException("Intervalo de km deve ser maior que zero.");
        this.intervaloKm = intervaloKm;
    }

    public void setUltimaRevisaoKm(int ultimaRevisaoKm) {
        if (ultimaRevisaoKm < 0) throw new DadosInvalidosException("Km da última revisão não pode ser negativo.");
        this.ultimaRevisaoKm = ultimaRevisaoKm;
    }

    public void setKmAtual(int kmAtual) {
        if (kmAtual < 0) throw new DadosInvalidosException("Km atual não pode ser negativo.");
        this.kmAtual = kmAtual;
    }

    @Override
    public String toString() {
        return "ServicoPreventivo{" + super.toString() +
                ", intervalo=" + intervaloKm + "km" +
                ", últimaRev=" + ultimaRevisaoKm + "km" +
                ", kmAtual=" + kmAtual +
                ", vencido=" + estaVencido() +
                ", custo=R$ " + calcularCusto() + "}";
    }
}
