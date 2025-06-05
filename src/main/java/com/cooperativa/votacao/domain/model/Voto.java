package com.cooperativa.votacao.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Voto {
    @Id
    private String id;

    private String pautaId;

    private String associadoId;
    
    private boolean votoFavoravel;
    private LocalDateTime dataVoto;
} 