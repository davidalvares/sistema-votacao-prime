package com.cooperativa.votacao.application.dto;

import jakarta.validation.constraints.NotBlank;

public record AssociadoRequest(
    @NotBlank(message = "O nome é obrigatório")
    String nome,
    
    @NotBlank(message = "O CPF é obrigatório")
    String cpf
) {} 