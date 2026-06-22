package model;

import java.math.BigDecimal;

public class Peca {
    private final int id;
    private String codigo;
    private String nome;
    private BigDecimal preco;
    private int estoque;

    public Peca(int id, String codigo, String nome, BigDecimal preco, int estoque) {
        this.id = id;
        setCodigo(codigo);
        setNome(nome);
        setPreco(preco);
        setEstoque(estoque);
    }

    /** Reserva (baixa) a quantidade informada do estoque. */
    public void reservar(int quantidade) {
        if (quantidade <= 0) throw new DadosInvalidosException("Quantidade deve ser maior que zero.");
        if (quantidade > estoque)
            throw new EstoqueInsuficienteException("Estoque insuficiente para '" + nome + "'. Disponível: " + estoque);
        estoque -= quantidade;
    }

    /** Repõe a quantidade informada no estoque. */
    public void repor(int quantidade) {
        if (quantidade <= 0) throw new DadosInvalidosException("Quantidade para reposição deve ser maior que zero.");
        estoque += quantidade;
    }

    /** Retorna true se o estoque está zerado. */
    public boolean estaEmFalta() {
        return estoque == 0;
    }

    public int getId() { return id; }

    public String getCodigo() { return codigo; }

    public String getNome() { return nome; }

    public BigDecimal getPreco() { return preco; }

    public int getEstoque() { return estoque; }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty())
            throw new DadosInvalidosException("Código da peça não pode ser vazio.");
        this.codigo = codigo.trim().toUpperCase();
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().length() < 2)
            throw new DadosInvalidosException("Nome da peça deve ter pelo menos 2 caracteres.");
        this.nome = nome.trim();
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0)
            throw new DadosInvalidosException("Preço da peça não pode ser negativo.");
        this.preco = preco;
    }

    public void setEstoque(int estoque) {
        if (estoque < 0) throw new DadosInvalidosException("Estoque não pode ser negativo.");
        this.estoque = estoque;
    }

    @Override
    public String toString() {
        return "Peca{id=" + id + ", cod='" + codigo + "', nome='" + nome +
                "', preço=R$ " + preco + ", estoque=" + estoque +
                (estaEmFalta() ? " [SEM ESTOQUE]" : "") + "}";
    }
}
