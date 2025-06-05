package com.cooperativa.votacao.domain.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class Pauta {
    
    @Id
    private String id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    private LocalDateTime dataCriacao;
    private LocalDateTime inicioSessao;
    private LocalDateTime fimSessao;
    private boolean sessaoAberta;

    public boolean isSessaoAberta() {
        if (!sessaoAberta) {
            return false;
        }
        
        LocalDateTime agora = LocalDateTime.now();
        return inicioSessao != null && 
               fimSessao != null && 
               agora.isAfter(inicioSessao) && 
               agora.isBefore(fimSessao);
    }
} 