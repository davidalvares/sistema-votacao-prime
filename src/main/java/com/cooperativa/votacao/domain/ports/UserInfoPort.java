package com.cooperativa.votacao.domain.ports;

import com.cooperativa.votacao.domain.model.StatusUsuario;

public interface UserInfoPort {
    StatusUsuario verificarCPF(String cpf);
} 