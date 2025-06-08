package com.cooperativa.votacao.infrastructure.rest;

import com.cooperativa.votacao.application.dto.AssociadoRequest;
import com.cooperativa.votacao.application.service.AssociadoService;
import com.cooperativa.votacao.domain.model.Associado;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/associados")
@RequiredArgsConstructor
public class AssociadoController {
    
    private final AssociadoService associadoService;
    
    @PostMapping
    public ResponseEntity<Associado> cadastrarAssociado(@Valid @RequestBody AssociadoRequest request) {
        Associado associado = associadoService.cadastrarAssociado(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(associado.getId())
            .toUri();
        return ResponseEntity.created(location).body(associado);
    }
} 