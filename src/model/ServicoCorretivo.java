package model;

import java.math.BigDecimal;

public class ServicoCorretivo extends Servico {
    private NivelUrgencia nivelUrgencia;
    private Especialidade categoria;
    private Peca pecaSubstituida;

    public ServicoCorretivo(int id, String descricao, BigDecimal precoBase,
                             NivelUrgencia nivelUrgencia, Especialidade categoria) {
        super(id, descricao, precoBase);
        setNivelUrgencia(nivelUrgencia);
        setCategoria(categoria);
    }

    /**
     * Aplica acréscimo conforme nível de urgência:
     * BAIXA → preço base, MEDIA → +30%, ALTA → +60%.
     */
    @Override
    public BigDecimal calcularCusto() {
        switch (nivelUrgencia) {
            case BAIXA: return getPrecoBase();
            case ALTA:  return getPrecoBase().multiply(new BigDecimal("1.60"));
            default:    return getPrecoBase().multiply(new BigDecimal("1.30")); // MEDIA
        }
    }

    /**
     * RN-03: retorna true se o serviço deve bloquear o veículo
     * (urgência ALTA em execução impede nova OS no veículo).
     */
    public boolean bloquearVeiculo() {
        return nivelUrgencia == NivelUrgencia.ALTA;
    }

    public NivelUrgencia getNivelUrgencia() { return nivelUrgencia; }

    public Especialidade getCategoria() { return categoria; }

    public Peca getPecaSubstituida() { return pecaSubstituida; }

    public void setNivelUrgencia(NivelUrgencia nivelUrgencia) {
        if (nivelUrgencia == null) throw new DadosInvalidosException("Nível de urgência não pode ser nulo.");
        this.nivelUrgencia = nivelUrgencia;
    }

    public void setCategoria(Especialidade categoria) {
        if (categoria == null) throw new DadosInvalidosException("Categoria do serviço não pode ser nula.");
        this.categoria = categoria;
    }

    public void setPecaSubstituida(Peca pecaSubstituida) {
        this.pecaSubstituida = pecaSubstituida;
    }

    @Override
    public String toString() {
        return "ServicoCorretivo{" + super.toString() +
                ", urgência=" + nivelUrgencia +
                ", categoria=" + categoria +
                ", pecaSubs=" + (pecaSubstituida != null ? pecaSubstituida.getNome() : "nenhuma") +
                ", custo=R$ " + calcularCusto() + "}";
    }
}
