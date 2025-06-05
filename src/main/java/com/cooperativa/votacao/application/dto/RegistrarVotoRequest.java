package com.cooperativa.votacao.application.dto;

import jakarta.validation.constraints.NotNull;

public record RegistrarVotoRequest(
    @NotNull(message = "O ID da pauta é obrigatório")
    String pautaId,
    
    @NotNull(message = "O ID do associado é obrigatório")
    String associadoId,
    
    @NotNull(message = "O voto é obrigatório")
    boolean votoFavoravel
) {} 