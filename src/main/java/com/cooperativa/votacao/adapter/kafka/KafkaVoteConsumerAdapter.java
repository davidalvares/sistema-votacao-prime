package com.cooperativa.votacao.adapter.kafka;

import com.cooperativa.votacao.application.dto.VoteMessage;
import com.cooperativa.votacao.application.service.VotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!dev")
public class KafkaVoteConsumerAdapter {

    private final VotoService voteService;

    @KafkaListener(topics = "votacao-votes", groupId = "votacao-processor-group", containerFactory = "voteKafkaListenerContainerFactory")
    public void processVote(VoteMessage vote) {
        log.info("Processing vote: {}", vote);
        try {
            voteService.processarVoto(vote.pautaId(), vote.associadoId(), vote.votoFavoravel());
            log.info("Voto processado com sucesso para o associado: {} and Pauta: {}", vote.associadoId(), vote.pautaId());
        } catch (Exception e) {
            log.error("Error processing vote for associado ID: {} and Pauta: {}. Error: {}",
                vote.associadoId(), vote.pautaId(), e.getMessage());
        }
    }
} 