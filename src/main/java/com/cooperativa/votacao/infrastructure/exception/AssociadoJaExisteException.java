package com.cooperativa.votacao.infrastructure.exception;

public class AssociadoJaExisteException extends RuntimeException {
    public AssociadoJaExisteException(String message) {
        super(message);
    }
} 