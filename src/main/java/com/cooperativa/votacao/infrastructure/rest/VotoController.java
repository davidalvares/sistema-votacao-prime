package com.cooperativa.votacao.infrastructure.rest;

import com.cooperativa.votacao.application.dto.RegistrarVotoRequest;
import com.cooperativa.votacao.application.service.VotoService;
import com.cooperativa.votacao.application.service.VotoService.ResultadoVotacao;
import com.cooperativa.votacao.domain.model.Voto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votos")
@RequiredArgsConstructor
public class VotoController {
    
    private final VotoService votoService;
    
    @PostMapping
    public ResponseEntity<Voto> registrarVoto(@Valid @RequestBody RegistrarVotoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(votoService.registrarVoto(request));
    }
    
    @GetMapping("/resultado/{pautaId}")
    public ResponseEntity<ResultadoVotacao> obterResultado(@PathVariable String pautaId) {
        return ResponseEntity.ok(votoService.obterResultado(pautaId));
    }
}