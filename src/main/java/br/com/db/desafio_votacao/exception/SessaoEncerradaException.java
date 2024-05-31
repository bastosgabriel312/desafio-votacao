package br.com.db.desafio_votacao.exception;

public class SessaoEncerradaException extends RuntimeException{
    public SessaoEncerradaException(String message) {
        super(message);
    }
}
