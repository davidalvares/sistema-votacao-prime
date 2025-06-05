//package com.cooperativa.votacao.domain.model;
//
//import org.junit.jupiter.api.Test;
//import java.time.LocalDateTime;
//import static org.junit.jupiter.api.Assertions.*;
//
//class VotoTest {
//
//    @Test
//    void deveCriarVotoComSucesso() {
//        // given
//        Pauta pauta = new Pauta();
//        pauta.setTitulo("Pauta Teste");
//
//        Associado associado = new Associado(1L, "João", "12345678909");
//
//        Voto voto = new Voto();
//        voto.setPauta(pauta);
//        voto.setAssociado(associado);
//        voto.setVotoFavoravel(true);
//
//        // then
//        assertNotNull(voto);
//        assertEquals(pauta, voto.getPauta());
//        assertEquals(associado, voto.getAssociado());
//        assertTrue(voto.isVotoFavoravel());
//    }
//
//    @Test
//    void deveDefinirDataVotoAoExecutarPrePersist() {
//        // given
//        Voto voto = new Voto();
//
//        // when
//        voto.prePersist();
//
//        // then
//        assertNotNull(voto.getDataVoto());
//    }
//
//    @Test
//    void devePermitirVotoContrario() {
//        // given
//        Voto voto = new Voto();
//
//        // when
//        voto.setVotoFavoravel(false);
//
//        // then
//        assertFalse(voto.isVotoFavoravel());
//    }
//
//    @Test
//    void deveManterReferenciaCorretaComPauta() {
//        // given
//        Pauta pauta = new Pauta();
//        pauta.setTitulo("Pauta de Teste");
//        pauta.setDescricao("Descrição da Pauta de Teste");
//
//        Voto voto = new Voto();
//
//        // when
//        voto.setPauta(pauta);
//
//        // then
//        assertNotNull(voto.getPauta());
//        assertEquals("Pauta de Teste", voto.getPauta().getTitulo());
//        assertEquals("Descrição da Pauta de Teste", voto.getPauta().getDescricao());
//    }
//
//    @Test
//    void deveManterReferenciaCorretaComAssociado() {
//        // given
//        Associado associado = new Associado(1L, "João", "12345678909");
//        Voto voto = new Voto();
//
//        // when
//        voto.setAssociado(associado);
//
//        // then
//        assertNotNull(voto.getAssociado());
//        assertEquals("João", voto.getAssociado().getNome());
//        assertEquals("12345678909", voto.getAssociado().getCpf());
//    }
//}