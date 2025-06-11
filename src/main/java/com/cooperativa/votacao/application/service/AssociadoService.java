package com.cooperativa.votacao.application.service;

import com.cooperativa.votacao.application.dto.AssociadoRequest;
import com.cooperativa.votacao.domain.model.Associado;
import com.cooperativa.votacao.domain.model.StatusUsuario;
import com.cooperativa.votacao.domain.repository.AssociadoRepository;
import com.cooperativa.votacao.infrastructure.client.UsuarioInfoClient;
import com.cooperativa.votacao.infrastructure.exception.AssociadoJaExisteException;
import com.cooperativa.votacao.infrastructure.exception.AssociadoNaoEncontradoException;
import com.cooperativa.votacao.infrastructure.exception.CpfInvalidoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssociadoService {
    
    private final AssociadoRepository associadoRepository;
    private final UsuarioInfoClient usuarioInfoClient;
    
    @Transactional
    public Associado cadastrarAssociado(AssociadoRequest request) {
        if (associadoRepository.existsByCpf(request.cpf())) {
            throw new AssociadoJaExisteException("CPF já cadastrado");
        }
        
        StatusUsuario status = usuarioInfoClient.verificarCPF(request.cpf());
        if (status != StatusUsuario.ABLE_TO_VOTE) {
            throw new CpfInvalidoException("CPF não está habilitado para votar");
        }
        
        Associado associado = new Associado();
        associado.setNome(request.nome());
        associado.setCpf(request.cpf());
        
        return associadoRepository.save(associado);
    }
    
    public Associado buscarPorId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do associado não pode ser nulo");
        }
        return associadoRepository.findById(id)
            .orElseThrow(() -> new AssociadoNaoEncontradoException("Associado não encontrado"));
    }

    public List<Associado> findAll() {
        return associadoRepository.findAll();
    }
} 