package com.cooperativa.votacao.application.dto;

public record VoteMessage(
    String associadoId,
    String pautaId,
    Boolean votoFavoravel
) {} 