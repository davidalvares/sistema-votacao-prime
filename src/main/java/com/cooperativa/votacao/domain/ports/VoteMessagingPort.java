package com.cooperativa.votacao.domain.ports;

import com.cooperativa.votacao.application.dto.VoteMessage;

public interface VoteMessagingPort {
    void sendVote(VoteMessage vote);
}
