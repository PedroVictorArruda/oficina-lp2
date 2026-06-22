package model;

public class TransicaoEstadoInvalidaException extends RuntimeException {
    public TransicaoEstadoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
