package com.cooperativa.votacao.infrastructure.client;

import com.cooperativa.votacao.domain.model.StatusUsuario;
import org.springframework.stereotype.Component;

@Component
//@Profile("test")
public class MockUsuarioInfoClient implements UsuarioInfoClient {
    @Override
    public StatusUsuario verificarCPF(String cpf) {
        return cpf.endsWith("0") ? StatusUsuario.UNABLE_TO_VOTE : StatusUsuario.ABLE_TO_VOTE;
    }
} 