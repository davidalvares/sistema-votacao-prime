package com.cooperativa.votacao.domain.ports;

import com.cooperativa.votacao.domain.model.Pauta;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PautaRepository extends MongoRepository<Pauta, String> {
} 