package com.cooperativa.votacao.application.service;

import com.cooperativa.votacao.domain.model.Pauta;
import com.cooperativa.votacao.domain.ports.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PautaService {
    
    private final PautaRepository pautaRepository;
    
    @Transactional
    public Pauta criar(String titulo, String descricao) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(titulo);
        pauta.setDescricao(descricao);
        pauta.setDataCriacao(LocalDateTime.now());
        pauta.setSessaoAberta(false);
        return pautaRepository.save(pauta);
    }
    
    @Transactional
    public Pauta abrirSessao(String pautaId, int duracaoMinutos) {
        Pauta pauta = buscarPorId(pautaId);
        
        if (pauta.isSessaoAberta()) {
            throw new IllegalStateException("Sessão já está aberta");
        }
        
        LocalDateTime agora = LocalDateTime.now();
        pauta.setInicioSessao(agora);
        pauta.setFimSessao(agora.plusMinutes(duracaoMinutos));
        pauta.setSessaoAberta(true);
        
        return pautaRepository.save(pauta);
    }
    
    public Pauta buscarPorId(String id) {
        return pautaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));
    }
} 