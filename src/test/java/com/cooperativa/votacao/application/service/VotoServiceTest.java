package com.cooperativa.votacao.application.service;

import com.cooperativa.votacao.domain.model.Pauta;
import com.cooperativa.votacao.domain.model.Voto;
import com.cooperativa.votacao.domain.model.Associado;
import com.cooperativa.votacao.domain.ports.VotoRepository;
import com.cooperativa.votacao.domain.ports.VoteMessagingPort;
import com.cooperativa.votacao.application.dto.RegistrarVotoRequest;
import com.cooperativa.votacao.application.dto.VoteMessage;
import com.cooperativa.votacao.infrastructure.exception.VotoJaRegistradoException;
import com.cooperativa.votacao.infrastructure.exception.SessaoNaoAbertaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaService pautaService;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private VoteMessagingPort voteMessagingPort;

    @InjectMocks
    private VotoService votoService;

    private final String PAUTA_ID = "1";
    private final String ASSOCIADO_ID = "123";

    private Pauta criarPautaAberta() {
        Pauta pauta = new Pauta();
        pauta.setId(PAUTA_ID);
        pauta.setSessaoAberta(true);
        pauta.setInicioSessao(LocalDateTime.now().minusMinutes(1));
        pauta.setFimSessao(LocalDateTime.now().plusMinutes(5));
        return pauta;
    }

    private Pauta criarPautaFechada() {
        Pauta pauta = new Pauta();
        pauta.setId(PAUTA_ID);
        pauta.setSessaoAberta(false);
        pauta.setInicioSessao(LocalDateTime.now().minusMinutes(10));
        pauta.setFimSessao(LocalDateTime.now().minusMinutes(5));
        return pauta;
    }

    @Test
    void deveRegistrarVotoQuandoAssociadoHabilitado() {
        Pauta pauta = criarPautaAberta();
        Associado associado = new Associado();
        associado.setId(ASSOCIADO_ID);
        associado.setNome("João");
        associado.setCpf("12345678909");

        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(pauta);
        when(associadoService.buscarPorId(ASSOCIADO_ID)).thenReturn(associado);
        when(votoRepository.existsByPautaIdAndAssociadoId(PAUTA_ID, ASSOCIADO_ID)).thenReturn(false);
        doNothing().when(voteMessagingPort).sendVote(any(VoteMessage.class));

        Voto votoSalvo = new Voto();
        votoSalvo.setId("1");
        votoSalvo.setPautaId(PAUTA_ID);
        votoSalvo.setAssociadoId(ASSOCIADO_ID);
        votoSalvo.setVotoFavoravel(true);

        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);

        Voto voto = votoService.registrarVoto(request);

        assertNotNull(voto);
        assertEquals(PAUTA_ID, voto.getPautaId());
        assertEquals(ASSOCIADO_ID, voto.getAssociadoId());
        assertTrue(voto.isVotoFavoravel());
        verify(voteMessagingPort).sendVote(any(VoteMessage.class));
        verify(pautaService).buscarPorId(PAUTA_ID);
        verify(associadoService).buscarPorId(ASSOCIADO_ID);
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoJaVotou() {
        Pauta pauta = criarPautaAberta();
        Associado associado = new Associado();
        associado.setId(ASSOCIADO_ID);
        associado.setNome("João");
        associado.setCpf("12345678909");

        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(pauta);
        when(associadoService.buscarPorId(ASSOCIADO_ID)).thenReturn(associado);
        when(votoRepository.existsByPautaIdAndAssociadoId(PAUTA_ID, ASSOCIADO_ID)).thenReturn(true);

        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);

        assertThrows(VotoJaRegistradoException.class, () -> votoService.registrarVoto(request));
        verify(votoRepository, never()).save(any(Voto.class));
        verify(voteMessagingPort, never()).sendVote(any(VoteMessage.class));
    }

    @Test
    void deveLancarExcecaoQuandoSessaoFechada() {
        Pauta pauta = criarPautaFechada();
        Associado associado = new Associado();
        associado.setId(ASSOCIADO_ID);
        associado.setNome("João");
        associado.setCpf("12345678909");

        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(pauta);

        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);

        assertThrows(SessaoNaoAbertaException.class, () -> votoService.registrarVoto(request));
        verify(votoRepository, never()).save(any(Voto.class));
        verify(voteMessagingPort, never()).sendVote(any(VoteMessage.class));
    }

    @Test
    void deveContabilizarVotosCorretamente() {
        Pauta pauta = criarPautaAberta();
        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(pauta);

        Voto voto1 = new Voto();
        voto1.setId("1");
        voto1.setPautaId(PAUTA_ID);
        voto1.setAssociadoId("1");
        voto1.setVotoFavoravel(true);
        voto1.setDataVoto(LocalDateTime.now());

        Voto voto2 = new Voto();
        voto2.setId("2");
        voto2.setPautaId(PAUTA_ID);
        voto2.setAssociadoId("2");
        voto2.setVotoFavoravel(false);
        voto2.setDataVoto(LocalDateTime.now());

        Voto voto3 = new Voto();
        voto3.setId("3");
        voto3.setPautaId(PAUTA_ID);
        voto3.setAssociadoId("3");
        voto3.setVotoFavoravel(true);
        voto3.setDataVoto(LocalDateTime.now());

        when(votoRepository.findByPautaId(PAUTA_ID)).thenReturn(Arrays.asList(voto1, voto2, voto3));

        VotoService.ResultadoVotacao resultado = votoService.obterResultado(PAUTA_ID);

        assertNotNull(resultado);
        assertEquals(PAUTA_ID, resultado.pautaId());
        assertEquals(2L, resultado.votosSim());
        assertEquals(1L, resultado.votosNao());
        verify(pautaService).buscarPorId(PAUTA_ID);
        verify(votoRepository).findByPautaId(PAUTA_ID);
    }

    @Test
    void deveRetornarResultadoZeradoQuandoNaoHaVotos() {
        Pauta pauta = criarPautaAberta();
        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(pauta);
        when(votoRepository.findByPautaId(PAUTA_ID)).thenReturn(Collections.emptyList());

        VotoService.ResultadoVotacao resultado = votoService.obterResultado(PAUTA_ID);

        assertNotNull(resultado);
        assertEquals(PAUTA_ID, resultado.pautaId());
        assertEquals(0L, resultado.votosSim());
        assertEquals(0L, resultado.votosNao());
        verify(pautaService).buscarPorId(PAUTA_ID);
        verify(votoRepository).findByPautaId(PAUTA_ID);
    }

    @Test
    void deveProcessarVotoComSucesso() {
        Voto voto = new Voto();
        voto.setPautaId(PAUTA_ID);
        voto.setAssociadoId(ASSOCIADO_ID);
        voto.setVotoFavoravel(true);

        when(votoRepository.save(any(Voto.class))).thenReturn(voto);

        votoService.processarVoto(PAUTA_ID, ASSOCIADO_ID, true);

        verify(votoRepository).save(any(Voto.class));
    }
}