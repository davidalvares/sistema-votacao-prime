package com.cooperativa.votacao.domain.ports;

import com.cooperativa.votacao.domain.model.Voto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface VotoRepository extends MongoRepository<Voto, String> {
    boolean existsByPautaIdAndAssociadoId(String pautaId, String associadoId);
    List<Voto> findByPautaId(String pautaId);
} 