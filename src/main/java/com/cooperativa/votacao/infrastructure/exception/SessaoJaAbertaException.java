package com.cooperativa.votacao.infrastructure.exception;

public class SessaoJaAbertaException extends RuntimeException {
    public SessaoJaAbertaException(String message) {
        super(message);
    }
} 