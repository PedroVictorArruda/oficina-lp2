package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estoque {
    private final List<Peca> listaPecas = new ArrayList<>();
    private int estoqueMinimo;

    public Estoque(int estoqueMinimo) {
        if (estoqueMinimo < 0) throw new DadosInvalidosException("Estoque mínimo não pode ser negativo.");
        this.estoqueMinimo = estoqueMinimo;
    }

    public void adicionarPeca(Peca peca) {
        if (peca == null) throw new DadosInvalidosException("Peça não pode ser nula.");
        listaPecas.add(peca);
    }

    /** Verifica se a peça possui ao menos a quantidade solicitada disponível. */
    public boolean verificarDisponibilidade(Peca peca, int quantidade) {
        return peca != null && peca.getEstoque() >= quantidade;
    }

    /**
     * RN-06: exibe alerta para todas as peças cujo estoque está abaixo do mínimo configurado.
     */
    public void alertarReposicao() {
        boolean algumAlerta = false;
        for (Peca peca : listaPecas) {
            if (peca.getEstoque() < estoqueMinimo) {
                System.out.println("[ALERTA ESTOQUE] '" + peca.getNome() +
                        "' — estoque atual: " + peca.getEstoque() +
                        " (mínimo: " + estoqueMinimo + ")");
                algumAlerta = true;
            }
        }
        if (!algumAlerta) {
            System.out.println("Todos os itens estão com estoque adequado.");
        }
    }

    public List<Peca> getListaPecas() {
        return Collections.unmodifiableList(listaPecas);
    }

    public int getEstoqueMinimo() { return estoqueMinimo; }

    public void setEstoqueMinimo(int estoqueMinimo) {
        if (estoqueMinimo < 0) throw new DadosInvalidosException("Estoque mínimo não pode ser negativo.");
        this.estoqueMinimo = estoqueMinimo;
    }

    public Peca buscarPorId(int id) {
        return listaPecas.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new DadosInvalidosException("Peça não encontrada."));
    }

    @Override
    public String toString() {
        return "Estoque{peças=" + listaPecas.size() + ", mínimo=" + estoqueMinimo + "}";
    }
}
