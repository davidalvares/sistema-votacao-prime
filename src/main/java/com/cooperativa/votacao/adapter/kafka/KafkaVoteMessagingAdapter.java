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
        log.info("Sending vote message: {}", vote);
        kafkaTemplate.send(TOPIC, vote)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Vote message sent successfully: {}", result.getRecordMetadata());
                    } else {
                        log.error("Unable to send vote message: {}", ex.getMessage());
                    }
                });
    }
} 