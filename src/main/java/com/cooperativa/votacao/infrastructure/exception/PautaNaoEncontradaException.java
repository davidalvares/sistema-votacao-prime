package com.cooperativa.votacao.infrastructure.exception;

public class PautaNaoEncontradaException extends RuntimeException {
    public PautaNaoEncontradaException(String message) {
        super(message);
    }
} 