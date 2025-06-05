package com.cooperativa.votacao.infrastructure.client;

import com.cooperativa.votacao.domain.model.StatusUsuario;

public interface UsuarioInfoClient {
    StatusUsuario verificarCPF(String cpf);
} 