package com.cooperativa.votacao.adapter.kafka;

import com.cooperativa.votacao.application.dto.VoteMessage;
import com.cooperativa.votacao.domain.ports.VoteMessagingPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!dev")
public class KafkaVoteMessagingAdapter implements VoteMessagingPort {

    private final KafkaTemplate<String, VoteMessage> kafkaTemplate;
    private static final String TOPIC = "votacao-votes";

    @Override
    public void sendVote(VoteMessage vote) {
        log.info("Enviando mensagem de voto: {}", vote);
        kafkaTemplate.send(TOPIC, vote)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Mensagem de voto enviada com sucesso: {}", result.getRecordMetadata());
                    } else {
                        log.error("Não foi possível enviar a mensagem de voto: {}", ex.getMessage());
                    }
                });
    }
} 