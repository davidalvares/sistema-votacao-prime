package com.cooperativa.votacao.infrastructure.persistence;

import com.cooperativa.votacao.domain.model.Pauta;
import com.cooperativa.votacao.domain.ports.PautaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface JpaPautaRepository extends MongoRepository<Pauta, String>, PautaRepository {
} 