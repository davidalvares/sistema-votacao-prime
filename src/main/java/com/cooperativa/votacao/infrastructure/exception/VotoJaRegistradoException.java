package com.cooperativa.votacao.infrastructure.exception;

public class VotoJaRegistradoException extends RuntimeException {
    public VotoJaRegistradoException(String message) {
        super(message);
    }
} 