package br.com.db.desafio_votacao.exception;

public class VotoJaRegistradoException extends RuntimeException {
    public VotoJaRegistradoException(String message) {
        super(message);
    }
}
