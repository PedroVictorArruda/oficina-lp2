package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Servico {
    private final int id;
    private String descricao;
    private BigDecimal precoBase;
    private final List<Peca> pecas = new ArrayList<>();

    protected Servico(int id, String descricao, BigDecimal precoBase) {
        this.id = id;
        setDescricao(descricao);
        setPrecoBase(precoBase);
    }

    /**
     * Calcula o custo do serviço (mão de obra), aplicando regras específicas de cada subtipo.
     * Não inclui o valor das peças.
     */
    public abstract BigDecimal calcularCusto();

    public int getId() { return id; }

    public String getDescricao() { return descricao; }

    public BigDecimal getPrecoBase() { return precoBase; }

    public List<Peca> getPecas() { return Collections.unmodifiableList(pecas); }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().length() < 3)
            throw new DadosInvalidosException("Descrição do serviço deve ter pelo menos 3 caracteres.");
        this.descricao = descricao.trim();
    }

    public void setPrecoBase(BigDecimal precoBase) {
        if (precoBase == null || precoBase.compareTo(BigDecimal.ZERO) < 0)
            throw new DadosInvalidosException("Preço base não pode ser negativo.");
        this.precoBase = precoBase;
    }

    public void adicionarPeca(Peca peca) {
        if (peca == null) throw new DadosInvalidosException("Peça não pode ser nula.");
        pecas.add(peca);
    }

    /** Retorna o total do serviço: custo calculado + soma das peças. */
    public BigDecimal calcularTotal() {
        BigDecimal total = calcularCusto();
        for (Peca peca : pecas) {
            total = total.add(peca.getPreco());
        }
        return total;
    }

    @Override
    public String toString() {
        return "id=" + id + ", desc='" + descricao + "', preçoBase=R$ " + precoBase + ", peças=" + pecas.size();
    }
}
