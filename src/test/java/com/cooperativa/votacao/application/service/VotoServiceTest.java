//package com.cooperativa.votacao.application.service;
//
//import com.cooperativa.votacao.domain.model.Pauta;
//import com.cooperativa.votacao.domain.model.Voto;
//import com.cooperativa.votacao.domain.model.Associado;
//import com.cooperativa.votacao.domain.ports.VotoRepository;
//import com.cooperativa.votacao.application.dto.RegistrarVotoRequest;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class VotoServiceTest {
//
//    @Mock
//    private VotoRepository votoRepository;
//
//    @Mock
//    private PautaService pautaService;
//
//    @Mock
//    private AssociadoService associadoService;
//
//    @InjectMocks
//    private VotoService votoService;
//
//    private final Long PAUTA_ID = 1L;
//    private final Long ASSOCIADO_ID = 123L;
//
//    @Test
//    void deveRegistrarVotoQuandoAssociadoHabilitado() {
//        // given
//        Pauta pauta = criarPautaAberta();
//        Associado associado = new Associado(ASSOCIADO_ID, "João", "12345678909");
//
//        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(pauta);
//        when(associadoService.buscarPorId(ASSOCIADO_ID)).thenReturn(associado);
//        when(votoRepository.existsByPautaIdAndAssociado_Id(PAUTA_ID, ASSOCIADO_ID)).thenReturn(false);
//
//        Voto votoSalvo = new Voto();
//        votoSalvo.setId(1L);
//        votoSalvo.setPauta(pauta);
//        votoSalvo.setAssociado(associado);
//        votoSalvo.setVotoFavoravel(true);
//        when(votoRepository.save(any(Voto.class))).thenReturn(votoSalvo);
//
//        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);
//
//        // when
//        Voto voto = votoService.registrarVoto(request);
//
//        // then
//        assertNotNull(voto);
//        assertEquals(PAUTA_ID, voto.getPauta().getId());
//        assertEquals(ASSOCIADO_ID, voto.getAssociado().getId());
//        assertTrue(voto.isVotoFavoravel());
//        verify(votoRepository).save(any(Voto.class));
//        verify(pautaService).buscarPorId(PAUTA_ID);
//        verify(associadoService).buscarPorId(ASSOCIADO_ID);
//    }
//
//    @Test
//    void deveLancarExcecaoQuandoSessaoFechada() {
//        // given
//        Pauta pauta = new Pauta();
//        pauta.setId(PAUTA_ID);
//        pauta.setSessaoAberta(false);
//        pauta.setInicioSessao(LocalDateTime.now().minusMinutes(10));
//        pauta.setFimSessao(LocalDateTime.now().minusMinutes(5));
//
//        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(pauta);
//
//        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);
//
//        // when/then
//        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
//            votoService.registrarVoto(request);
//        });
//        assertEquals("Sessão de votação não está aberta", exception.getMessage());
//        verify(votoRepository, never()).save(any());
//        verify(associadoService, never()).buscarPorId(any());
//    }
//
//    @Test
//    void deveLancarExcecaoQuandoAssociadoJaVotou() {
//        // given
//        Pauta pauta = criarPautaAberta();
//        Associado associado = new Associado(ASSOCIADO_ID, "João", "12345678909");
//
//        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(pauta);
//        when(associadoService.buscarPorId(ASSOCIADO_ID)).thenReturn(associado);
//        when(votoRepository.existsByPautaIdAndAssociado_Id(PAUTA_ID, ASSOCIADO_ID)).thenReturn(true);
//
//        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);
//
//        // when/then
//        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
//            votoService.registrarVoto(request);
//        });
//        assertEquals("Associado já votou nesta pauta", exception.getMessage());
//        verify(votoRepository, never()).save(any());
//        verify(pautaService).buscarPorId(PAUTA_ID);
//        verify(associadoService).buscarPorId(ASSOCIADO_ID);
//    }
//
//    @Test
//    void deveContabilizarVotosCorretamente() {
//        // given
//        Pauta pauta = criarPautaAberta();
//        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(pauta);
//
//        Associado associado1 = new Associado(1L, "João", "12345678901");
//        Associado associado2 = new Associado(2L, "Maria", "12345678902");
//        Associado associado3 = new Associado(3L, "José", "12345678903");
//
//        Voto voto1 = new Voto();
//        voto1.setId("1");
//        voto1.setPauta(String.valueOf(pauta));
//        voto1.setAssociado(String.valueOf(associado1));
//        voto1.setVotoFavoravel(true);
//        voto1.setDataVoto(LocalDateTime.now());
//
//        Voto voto2 = new Voto();
//        voto2.setId("2");
//        voto2.setPauta(pauta);
//        voto2.setAssociado(associado2);
//        voto2.setVotoFavoravel(false);
//        voto2.setDataVoto(LocalDateTime.now());
//
//        Voto voto3 = new Voto();
//        voto3.setId(3L);
//        voto3.setPauta(pauta);
//        voto3.setAssociado(associado3);
//        voto3.setVotoFavoravel(true);
//        voto3.setDataVoto(LocalDateTime.now());
//
//        when(votoRepository.findByPautaId(PAUTA_ID)).thenReturn(Arrays.asList(voto1, voto2, voto3));
//
//        // when
//        VotoService.ResultadoVotacao resultado = votoService.obterResultado(PAUTA_ID);
//
//        // then
//        assertNotNull(resultado);
//        assertEquals(PAUTA_ID, resultado.pautaId());
//        assertEquals(2L, resultado.votosSim());
//        assertEquals(1L, resultado.votosNao());
//        assertEquals(3L, resultado.votosSim() + resultado.votosNao());
//
//        verify(pautaService).buscarPorId(PAUTA_ID);
//        verify(votoRepository).findByPautaId(PAUTA_ID);
//    }
//
//    private Pauta criarPautaAberta() {
//        LocalDateTime agora = LocalDateTime.now();
//        Pauta pauta = new Pauta();
//        pauta.setId(PAUTA_ID);
//        pauta.setSessaoAberta(true);
//        pauta.setInicioSessao(agora.minusMinutes(1));
//        pauta.setFimSessao(agora.plusMinutes(4));
//        return pauta;
//    }
//}