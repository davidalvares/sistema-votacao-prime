package com.cooperativa.votacao.infrastructure.rest;

import com.cooperativa.votacao.application.service.PautaService;
import com.cooperativa.votacao.domain.model.Pauta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pautas")
@RequiredArgsConstructor
public class PautaController {
    
    private final PautaService pautaService;
    
    @PostMapping
    public ResponseEntity<Pauta> criarPauta(@Valid @RequestBody CriarPautaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pautaService.criar(request.titulo(), request.descricao()));
    }
    
    @PostMapping("/{id}/abrir-sessao")
    public ResponseEntity<Pauta> abrirSessao(@PathVariable String id, @Valid @RequestBody AbrirSessaoRequest request) {
        return ResponseEntity.ok(pautaService.abrirSessao(id, request.duracaoMinutos()));
    }
    
    @GetMapping
    public ResponseEntity<List<Pauta>> listarPautas() {
        return ResponseEntity.ok(pautaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pauta> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(pautaService.buscarPorId(id));
    }
    
    public record CriarPautaRequest(
        @NotBlank(message = "O título é obrigatório")
        String titulo,
        
        @NotBlank(message = "A descrição é obrigatória")
        String descricao
    ) {}
    
    public record AbrirSessaoRequest(
        @NotNull(message = "A duração da sessão é obrigatória")
        @Min(value = 1, message = "A duração mínima da sessão é de 1 minuto")
        Integer duracaoMinutos
    ) {}
} 