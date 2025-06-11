package com.cooperativa.votacao.application.service;

import com.cooperativa.votacao.domain.model.Pauta;
import com.cooperativa.votacao.domain.ports.PautaRepository;
import com.cooperativa.votacao.infrastructure.exception.PautaNaoEncontradaException;
import com.cooperativa.votacao.infrastructure.exception.SessaoJaAbertaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

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
        verify(pautaRepository).save(any(Pauta.class));
    }

    @Test
    void deveLancarExcecaoAoAbrirSessaoQuandoPautaNaoEncontrada() {
        String pautaId = "1";
        int duracaoMinutos = 5;

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        assertThrows(PautaNaoEncontradaException.class, () -> pautaService.abrirSessao(pautaId, duracaoMinutos));
        verify(pautaRepository).findById(pautaId);
        verify(pautaRepository, never()).save(any(Pauta.class));
    }

    @Test
    void deveLancarExcecaoAoAbrirSessaoQuandoJaAberta() {
        String pautaId = "1";
        int duracaoMinutos = 5;

        Pauta pautaExistente = new Pauta();
        pautaExistente.setId(pautaId);
        pautaExistente.setSessaoAberta(true);
        pautaExistente.setInicioSessao(LocalDateTime.now().minusMinutes(1));
        pautaExistente.setFimSessao(LocalDateTime.now().plusMinutes(4));

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pautaExistente));

        assertThrows(SessaoJaAbertaException.class, () -> pautaService.abrirSessao(pautaId, duracaoMinutos));
        verify(pautaRepository).findById(pautaId);
        verify(pautaRepository, never()).save(any(Pauta.class));
    }

    @Test
    void deveListarTodasAsPautas() {
        Pauta pauta1 = new Pauta();
        pauta1.setId("1");
        pauta1.setTitulo("Pauta 1");
        pauta1.setDescricao("Descrição da Pauta 1");

        Pauta pauta2 = new Pauta();
        pauta2.setId("2");
        pauta2.setTitulo("Pauta 2");
        pauta2.setDescricao("Descrição da Pauta 2");

        List<Pauta> pautas = Arrays.asList(pauta1, pauta2);
        when(pautaRepository.findAll()).thenReturn(pautas);

        List<Pauta> resultado = pautaService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Pauta 1", resultado.get(0).getTitulo());
        assertEquals("Pauta 2", resultado.get(1).getTitulo());
        verify(pautaRepository).findAll();
    }

    @Test
    void deveBuscarPautaPorId() {
        String pautaId = "1";
        Pauta pauta = new Pauta();
        pauta.setId(pautaId);
        pauta.setTitulo("Pauta de Teste");
        pauta.setDescricao("Descrição da Pauta de Teste");

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        Pauta resultado = pautaService.buscarPorId(pautaId);

        assertNotNull(resultado);
        assertEquals(pautaId, resultado.getId());
        assertEquals("Pauta de Teste", resultado.getTitulo());
        verify(pautaRepository).findById(pautaId);
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        String pautaId = "1";
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        assertThrows(PautaNaoEncontradaException.class, () -> pautaService.buscarPorId(pautaId));
        verify(pautaRepository).findById(pautaId);
    }
}