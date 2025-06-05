package com.cooperativa.votacao.infrastructure.rest;

import com.cooperativa.votacao.application.service.PautaService;
import com.cooperativa.votacao.domain.model.Pauta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pautas")
@RequiredArgsConstructor
public class PautaController {
    
    private final PautaService pautaService;
    
    @PostMapping
    public ResponseEntity<Pauta> criarPauta(@Valid @RequestBody CriarPautaRequest request) {
        return ResponseEntity.ok(pautaService.criar(request.titulo(), request.descricao()));
    }
    
    @PostMapping("/{id}/sessao")
    public ResponseEntity<Pauta> abrirSessao(@PathVariable String id, @Valid @RequestBody AbrirSessaoRequest request) {
        return ResponseEntity.ok(pautaService.abrirSessao(id, request.duracaoMinutos()));
    }
    
    public record CriarPautaRequest(
        @NotBlank(message = "O título é obrigatório")
        String titulo,
        
        @NotBlank(message = "A descrição é obrigatória")
        String descricao
    ) {}
    
    public record AbrirSessaoRequest(
        @NotNull(message = "A duração em minutos é obrigatória")
        Integer duracaoMinutos
    ) {}
} 