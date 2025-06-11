package com.cooperativa.votacao.infrastructure.rest;

import com.cooperativa.votacao.application.dto.AssociadoRequest;
import com.cooperativa.votacao.application.service.AssociadoService;
import com.cooperativa.votacao.domain.model.Associado;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/associados")
@RequiredArgsConstructor
public class AssociadoController {
    
    private final AssociadoService associadoService;
    
    @PostMapping
    public ResponseEntity<Associado> cadastrarAssociado(@Valid @RequestBody AssociadoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(associadoService.cadastrarAssociado(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Associado> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(associadoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Associado>> listarTodos() {
        return ResponseEntity.ok(associadoService.findAll());
    }
} 