package com.cooperativa.votacao.application.service;

import com.cooperativa.votacao.application.dto.RegistrarVotoRequest;
import com.cooperativa.votacao.application.dto.VoteMessage;
import com.cooperativa.votacao.domain.model.Associado;
import com.cooperativa.votacao.domain.model.Pauta;
import com.cooperativa.votacao.domain.model.Voto;
import com.cooperativa.votacao.domain.ports.VoteMessagingPort;
import com.cooperativa.votacao.domain.ports.VotoRepository;
import com.cooperativa.votacao.infrastructure.exception.SessaoNaoAbertaException;
import com.cooperativa.votacao.infrastructure.exception.VotoJaRegistradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final PautaService pautaService;
    private final AssociadoService associadoService;
    private final VoteMessagingPort voteMessagingPort;

    @Transactional
    public Voto registrarVoto(RegistrarVotoRequest request) {
        Pauta pauta = pautaService.buscarPorId(request.pautaId());
        
        if (!pauta.isSessaoAberta()) {
            throw new SessaoNaoAbertaException("Sessão de votação não está aberta");
        }
        
        Associado associado = associadoService.buscarPorId(request.associadoId());
        
        if (votoRepository.existsByPautaIdAndAssociadoId(request.pautaId(), request.associadoId())) {
            throw new VotoJaRegistradoException("Associado já votou nesta pauta");
        }

        VoteMessage voteMessage = new VoteMessage(
                request.associadoId(),
                request.pautaId(),
                request.votoFavoravel()
        );

        voteMessagingPort.sendVote(voteMessage);
        
        Voto voto = new Voto();
        voto.setPautaId(pauta.getId());
        voto.setAssociadoId(associado.getId());
        voto.setVotoFavoravel(request.votoFavoravel());
        return voto;
    }

    public ResultadoVotacao obterResultado(String pautaId) {
        pautaService.buscarPorId(pautaId);
        
        var votos = votoRepository.findByPautaId(pautaId);
        Long votosSim = votos.stream()
            .filter(Voto::isVotoFavoravel)
            .count();
        Long votosNao = votos.stream()
            .filter(voto -> !voto.isVotoFavoravel())
            .count();
        
        return new ResultadoVotacao(pautaId, votosSim, votosNao);
    }

    public void processarVoto(String pautaId, String associadoId, boolean votoFavoravel) {

        Voto voto = new Voto();
        voto.setPautaId(pautaId);
        voto.setAssociadoId(associadoId);
        voto.setVotoFavoravel(votoFavoravel);

        votoRepository.save(voto);
    }

    public record ResultadoVotacao(String pautaId, Long votosSim, Long votosNao) {}
} 