package model;

public abstract class Pessoa {
    private final int id;
    private String nome;
    private String cpf;
    private String telefone;

    protected Pessoa(int id, String nome, String cpf, String telefone) {
        this.id = id;
        setNome(nome);
        // Armazena CPF com validação de formato apenas; validarCpf() é chamado após construção
        if (cpf == null || !cpf.matches("\\d{11}"))
            throw new DadosInvalidosException("CPF deve conter exatamente 11 dígitos numéricos.");
        this.cpf = cpf;
        setTelefone(telefone);
    }

    /**
     * Valida o CPF usando o algoritmo oficial brasileiro.
     * Implementado nas subclasses para demonstrar polimorfismo.
     */
    public abstract boolean validarCpf();

    public int getId() { return id; }

    public String getNome() { return nome; }

    public String getCpf() { return cpf; }

    public String getTelefone() { return telefone; }

    public void setNome(String nome) {
        if (nome == null || nome.trim().length() < 3)
            throw new DadosInvalidosException("Nome deve ter pelo menos 3 caracteres.");
        this.nome = nome.trim();
    }

    public void setCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}"))
            throw new DadosInvalidosException("CPF deve conter exatamente 11 dígitos numéricos.");
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || !telefone.matches("\\d{10,11}"))
            throw new DadosInvalidosException("Telefone deve conter 10 ou 11 dígitos numéricos.");
        this.telefone = telefone;
    }

    /** Algoritmo padrão de validação de CPF brasileiro — compartilhado pelas subclasses. */
    protected boolean algoritmoValidacaoCpf() {
        if (cpf == null || cpf.length() != 11) return false;
        if (cpf.chars().distinct().count() == 1) return false;

        int[] d = cpf.chars().map(c -> c - '0').toArray();

        int soma1 = 0;
        for (int i = 0; i < 9; i++) soma1 += d[i] * (10 - i);
        int dig1 = (soma1 * 10) % 11;
        if (dig1 == 10) dig1 = 0;

        int soma2 = 0;
        for (int i = 0; i < 10; i++) soma2 += d[i] * (11 - i);
        int dig2 = (soma2 * 10) % 11;
        if (dig2 == 10) dig2 = 0;

        return d[9] == dig1 && d[10] == dig2;
    }

    @Override
    public String toString() {
        return "id=" + id + ", nome='" + nome + "', cpf='" + cpf + "', tel='" + telefone + "'";
    }
}
