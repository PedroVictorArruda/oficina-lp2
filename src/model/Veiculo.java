package model;

import java.math.BigDecimal;

public abstract class Veiculo {
    private final int id;
    private String placa;
    private String modelo;
    private int ano;
    private Cliente dono;

    protected Veiculo(int id, String placa, String modelo, int ano, Cliente dono) {
        this.id = id;
        setPlaca(placa);
        setModelo(modelo);
        setAno(ano);
        setDono(dono);
    }

    /**
     * Calcula o custo base de mão de obra para este veículo.
     * Cada subtipo aplica seus próprios fatores (combustível, cilindrada, carga).
     */
    public abstract BigDecimal calcularCustoBase();

    public int getId() { return id; }

    public String getPlaca() { return placa; }

    public String getModelo() { return modelo; }

    public int getAno() { return ano; }

    public Cliente getDono() { return dono; }

    public void setPlaca(String placa) {
        if (placa == null || !placa.trim().toUpperCase().matches("[A-Z]{3}[0-9][A-Z0-9][0-9]{2}"))
            throw new DadosInvalidosException("Placa inválida. Use formato antigo ABC1234 ou Mercosul ABC1D23.");
        this.placa = placa.trim().toUpperCase();
    }

    public void setModelo(String modelo) {
        if (modelo == null || modelo.trim().length() < 2)
            throw new DadosInvalidosException("Modelo deve ter pelo menos 2 caracteres.");
        this.modelo = modelo.trim();
    }

    public void setAno(int ano) {
        int anoAtual = java.time.Year.now().getValue();
        if (ano < 1900 || ano > anoAtual + 1)
            throw new DadosInvalidosException("Ano deve estar entre 1900 e " + (anoAtual + 1) + ".");
        this.ano = ano;
    }

    public void setDono(Cliente dono) {
        if (dono == null) throw new DadosInvalidosException("Veículo deve estar vinculado a um cliente.");
        this.dono = dono;
    }

    @Override
    public String toString() {
        return "id=" + id + ", placa='" + placa + "', modelo='" + modelo +
                "', ano=" + ano + ", dono='" + dono.getNome() + "'";
    }
}
