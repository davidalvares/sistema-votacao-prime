package com.cooperativa.votacao.application.service;

import com.cooperativa.votacao.application.dto.RegistrarVotoRequest;
import com.cooperativa.votacao.domain.model.Associado;
import com.cooperativa.votacao.domain.model.Pauta;
import com.cooperativa.votacao.domain.model.Voto;
import com.cooperativa.votacao.domain.ports.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final PautaService pautaService;
    private final AssociadoService associadoService;

    @Transactional
    public Voto registrarVoto(RegistrarVotoRequest request) {
        Pauta pauta = pautaService.buscarPorId(request.pautaId());
        
        if (!pauta.isSessaoAberta()) {
            throw new IllegalStateException("Sessão de votação não está aberta");
        }
        
        Associado associado = associadoService.buscarPorId(request.associadoId());
        
        if (votoRepository.existsByPautaIdAndAssociadoId(request.pautaId(), request.associadoId())) {
            throw new IllegalStateException("Associado já votou nesta pauta");
        }
        
        Voto voto = new Voto();
        voto.setPautaId(pauta.getId());
        voto.setAssociadoId(associado.getId());
        voto.setVotoFavoravel(request.votoFavoravel());
        
        return votoRepository.save(voto);
    }

    public ResultadoVotacao obterResultado(String pautaId) {
        pautaService.buscarPorId(pautaId); // Verifica se a pauta existe
        
        var votos = votoRepository.findByPautaId(pautaId);
        Long votosSim = votos.stream()
            .filter(voto -> voto.isVotoFavoravel())
            .count();
        Long votosNao = votos.stream()
            .filter(voto -> !voto.isVotoFavoravel())
            .count();
        
        return new ResultadoVotacao(pautaId, votosSim, votosNao);
    }

    public record ResultadoVotacao(String pautaId, Long votosSim, Long votosNao) {
        public Long getTotalVotos() {
            return votosSim + votosNao;
        }
    }
} 