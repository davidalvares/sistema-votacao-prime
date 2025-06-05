package com.cooperativa.votacao.infrastructure.persistence;

import com.cooperativa.votacao.domain.model.Associado;
import com.cooperativa.votacao.domain.repository.AssociadoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface JpaAssociadoRepository extends MongoRepository<Associado, String>, AssociadoRepository {
} 