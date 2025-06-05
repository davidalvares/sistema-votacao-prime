package com.cooperativa.votacao.domain.repository;

import com.cooperativa.votacao.domain.model.Associado;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AssociadoRepository extends MongoRepository<Associado, String> {
    boolean existsByCpf(String cpf);
} 