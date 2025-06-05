//package com.cooperativa.votacao.infrastructure.rest;
//
//import com.cooperativa.votacao.application.service.PautaService;
//import com.cooperativa.votacao.domain.model.Pauta;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PautaControllerTest {
//
//    private static final LocalDateTime DATA_FIXA = LocalDateTime.of(2024, 1, 1, 10, 0);
//
//    @Mock
//    private PautaService pautaService;
//
//    @InjectMocks
//    private PautaController pautaController;
//
//    @Test
//    void deveCriarPautaComSucesso() {
//
//        String titulo = "Título da Pauta";
//        String descricao = "Descrição da Pauta";
//
//        Pauta pautaEsperada = new Pauta();
//        pautaEsperada.setId(1L);
//        pautaEsperada.setTitulo(titulo);
//        pautaEsperada.setDescricao(descricao);
//        pautaEsperada.setDataCriacao(DATA_FIXA);
//        pautaEsperada.setSessaoAberta(false);
//
//        when(pautaService.criar(eq(titulo), eq(descricao)))
//            .thenReturn(pautaEsperada);
//
//
//        ResponseEntity<Pauta> response = pautaController.criarPauta(
//            new PautaController.CriarPautaRequest(titulo, descricao)
//        );
//
//
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(pautaEsperada.getId(), response.getBody().getId());
//        assertEquals(titulo, response.getBody().getTitulo());
//        assertEquals(descricao, response.getBody().getDescricao());
//        assertFalse(response.getBody().isSessaoAberta());
//        verify(pautaService).criar(titulo, descricao);
//    }
//
//    @Test
//    void deveAbrirSessaoComSucesso() {
//        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
//
//            mockedStatic.when(LocalDateTime::now).thenReturn(DATA_FIXA);
//
//            Long pautaId = 1L;
//            Integer duracaoMinutos = 5;
//
//            Pauta pautaEsperada = new Pauta();
//            pautaEsperada.setId(pautaId);
//            pautaEsperada.setSessaoAberta(true);
//            pautaEsperada.setInicioSessao(DATA_FIXA.minusMinutes(1));
//            pautaEsperada.setFimSessao(DATA_FIXA.plusMinutes(duracaoMinutos));
//
//            when(pautaService.abrirSessao(eq(pautaId), eq(duracaoMinutos)))
//                .thenReturn(pautaEsperada);
//
//
//            ResponseEntity<Pauta> response = pautaController.abrirSessao(
//                pautaId,
//                new PautaController.AbrirSessaoRequest(duracaoMinutos)
//            );
//
//
//            assertNotNull(response);
//            assertEquals(HttpStatus.OK, response.getStatusCode());
//            assertNotNull(response.getBody());
//            assertEquals(pautaId, response.getBody().getId());
//            assertTrue(response.getBody().isSessaoAberta());
//            assertNotNull(response.getBody().getInicioSessao());
//            assertNotNull(response.getBody().getFimSessao());
//            verify(pautaService).abrirSessao(pautaId, duracaoMinutos);
//        }
//    }
//
//    @Test
//    void deveAbrirSessaoComDuracaoPadrao() {
//        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
//
//            mockedStatic.when(LocalDateTime::now).thenReturn(DATA_FIXA);
//
//            Long pautaId = 1L;
//            int duracaoPadrao = 1;
//
//            Pauta pautaEsperada = new Pauta();
//            pautaEsperada.setId(pautaId);
//            pautaEsperada.setSessaoAberta(true);
//            pautaEsperada.setInicioSessao(DATA_FIXA.minusMinutes(1));
//            pautaEsperada.setFimSessao(DATA_FIXA.plusMinutes(duracaoPadrao));
//
//            when(pautaService.abrirSessao(eq(pautaId), eq(duracaoPadrao)))
//                .thenReturn(pautaEsperada);
//
//
//            ResponseEntity<Pauta> response = pautaController.abrirSessao(
//                pautaId,
//                new PautaController.AbrirSessaoRequest(duracaoPadrao)
//            );
//
//
//            assertNotNull(response);
//            assertEquals(HttpStatus.OK, response.getStatusCode());
//            assertNotNull(response.getBody());
//            assertEquals(pautaId, response.getBody().getId());
//            assertTrue(response.getBody().isSessaoAberta());
//            assertNotNull(response.getBody().getInicioSessao());
//            assertNotNull(response.getBody().getFimSessao());
//            verify(pautaService).abrirSessao(pautaId, duracaoPadrao);
//        }
//    }
//
//}