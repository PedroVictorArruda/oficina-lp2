package model;

import java.math.BigDecimal;

public class Mecanico extends Pessoa {
    private Especialidade especialidade;
    private BigDecimal salario;

    public Mecanico(int id, String nome, String cpf, String telefone,
                    Especialidade especialidade, BigDecimal salario) {
        super(id, nome, cpf, telefone);
        setEspecialidade(especialidade);
        setSalario(salario);
    }

    @Override
    public boolean validarCpf() {
        return algoritmoValidacaoCpf();
    }

    /** Retorna 5% do salário como comissão por OS concluída. */
    public BigDecimal calcularComissao() {
        return salario.multiply(new BigDecimal("0.05"));
    }

    /**
     * RN-01: mecânico pode executar o serviço se sua especialidade for GERAL
     * ou coincidir com a especialidade exigida.
     */
    public boolean podeExecutar(Especialidade especialidadeExigida) {
        return this.especialidade == Especialidade.GERAL
                || this.especialidade == especialidadeExigida;
    }

    public Especialidade getEspecialidade() { return especialidade; }

    public BigDecimal getSalario() { return salario; }

    public void setEspecialidade(Especialidade especialidade) {
        if (especialidade == null) throw new DadosInvalidosException("Especialidade não pode ser nula.");
        this.especialidade = especialidade;
    }

    public void setSalario(BigDecimal salario) {
        if (salario == null || salario.compareTo(BigDecimal.ZERO) < 0)
            throw new DadosInvalidosException("Salário não pode ser negativo.");
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Mecanico{" + super.toString() +
                ", especialidade=" + especialidade +
                ", salário=R$ " + salario + "}";
    }
}
