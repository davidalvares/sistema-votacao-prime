package com.cooperativa.votacao.infrastructure.persistence;

import com.cooperativa.votacao.domain.model.Voto;
import com.cooperativa.votacao.domain.ports.VotoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JpaVotoRepository extends MongoRepository<Voto, String>, VotoRepository {
} 