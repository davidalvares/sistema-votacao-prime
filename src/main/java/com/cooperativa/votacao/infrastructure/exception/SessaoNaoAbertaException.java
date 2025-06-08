package com.cooperativa.votacao.infrastructure.exception;

public class SessaoNaoAbertaException extends RuntimeException {
    public SessaoNaoAbertaException(String message) {
        super(message);
    }
} 