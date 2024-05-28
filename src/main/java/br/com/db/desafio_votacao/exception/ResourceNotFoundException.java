package br.com.db.desafio_votacao.exception;

import jakarta.persistence.Entity;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Class<?> entidade) {
        super(entidade.getName()+" não encontrado (a)");
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}