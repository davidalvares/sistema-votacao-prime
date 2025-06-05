package com.cooperativa.votacao.application.service;

import com.cooperativa.votacao.domain.model.Pauta;
import com.cooperativa.votacao.domain.ports.PautaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    void deveCriarPautaComSucesso() {

        String titulo = "Título da Pauta";
        String descricao = "Descrição da Pauta";
        
        Pauta pautaSalva = new Pauta();
        pautaSalva.setId("1");
        pautaSalva.setTitulo(titulo);
        pautaSalva.setDescricao(descricao);
        pautaSalva.setDataCriacao(LocalDateTime.now());
        pautaSalva.setSessaoAberta(false);

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaSalva);


        Pauta resultado = pautaService.criar(titulo, descricao);


        assertNotNull(resultado);
        assertEquals(titulo, resultado.getTitulo());
        assertEquals(descricao, resultado.getDescricao());
        assertFalse(resultado.isSessaoAberta());
        assertNotNull(resultado.getDataCriacao());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    void deveAbrirSessaoComSucesso() {

        String pautaId = "1";
        Integer duracaoMinutos = 5;
        
        Pauta pautaExistente = new Pauta();
        pautaExistente.setId(pautaId);
        pautaExistente.setSessaoAberta(false);

        Pauta pautaSalva = new Pauta();
        pautaSalva.setId(pautaId);
        pautaSalva.setSessaoAberta(true);
        pautaSalva.setInicioSessao(LocalDateTime.now().minusMinutes(1));
        pautaSalva.setFimSessao(LocalDateTime.now().plusMinutes(duracaoMinutos));

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pautaExistente));
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaSalva);


        Pauta resultado = pautaService.abrirSessao(pautaId, duracaoMinutos);


        assertNotNull(resultado);
        assertTrue(resultado.isSessaoAberta());
        assertNotNull(resultado.getInicioSessao());
        assertNotNull(resultado.getFimSessao());
        assertTrue(resultado.getFimSessao().isAfter(resultado.getInicioSessao()));
        verify(pautaRepository, times(1)).findById(pautaId);
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    void deveAbrirSessaoComDuracaoPadrao() {

        String pautaId = "1";
        int duracaoPadrao = 1;
        LocalDateTime inicioSessao = LocalDateTime.now();
        
        Pauta pautaExistente = new Pauta();
        pautaExistente.setId(pautaId);
        pautaExistente.setSessaoAberta(false);

        Pauta pautaSalva = new Pauta();
        pautaSalva.setId(pautaId);
        pautaSalva.setSessaoAberta(true);
        pautaSalva.setInicioSessao(inicioSessao.minusMinutes(1));
        pautaSalva.setFimSessao(inicioSessao.plusMinutes(duracaoPadrao));

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pautaExistente));
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaSalva);


        Pauta resultado = pautaService.abrirSessao(pautaId, duracaoPadrao);


        assertNotNull(resultado);
        assertTrue(resultado.isSessaoAberta());
        assertNotNull(resultado.getInicioSessao());
        assertNotNull(resultado.getFimSessao());
        verify(pautaRepository, times(1)).findById(pautaId);
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    void deveLancarExcecaoAoAbrirSessaoEmPautaInexistente() {

        String pautaId = "1";
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> pautaService.abrirSessao(pautaId, 5));
        assertEquals("Pauta não encontrada", exception.getMessage());
        verify(pautaRepository, times(1)).findById(pautaId);
        verify(pautaRepository, never()).save(any(Pauta.class));
    }

    @Test
    void deveLancarExcecaoAoAbrirSessaoJaAberta() {

        String pautaId = "1";
        LocalDateTime agora = LocalDateTime.now();
        
        Pauta pautaExistente = new Pauta();
        pautaExistente.setId(pautaId);
        pautaExistente.setSessaoAberta(true);
        pautaExistente.setInicioSessao(agora.minusMinutes(5));
        pautaExistente.setFimSessao(agora.plusMinutes(5));

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pautaExistente));


        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> pautaService.abrirSessao(pautaId, 5));
        assertEquals("Sessão já está aberta", exception.getMessage());
        verify(pautaRepository, times(1)).findById(pautaId);
        verify(pautaRepository, never()).save(any(Pauta.class));
    }
} 