package com.cooperativa.votacao.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class VotoTest {

    @Test
    void deveCriarVotoComSucesso() {
        Pauta pauta = new Pauta();
        pauta.setId("1");
        pauta.setTitulo("Pauta Teste");

        Associado associado = new Associado();
        associado.setId("1");
        associado.setNome("João");
        associado.setCpf("12345678909");

        Voto voto = new Voto();
        voto.setPautaId(pauta.getId());
        voto.setAssociadoId(associado.getId());
        voto.setVotoFavoravel(true);

        assertNotNull(voto);
        assertEquals(pauta.getId(), voto.getPautaId());
        assertEquals(associado.getId(), voto.getAssociadoId());
        assertTrue(voto.isVotoFavoravel());
    }

    @Test
    void devePermitirVotoContrario() {
        Voto voto = new Voto();
        voto.setVotoFavoravel(false);
        assertFalse(voto.isVotoFavoravel());
    }

    @Test
    void deveManterReferenciaCorretaComPauta() {
        Pauta pauta = new Pauta();
        pauta.setId("1");
        pauta.setTitulo("Pauta de Teste");
        pauta.setDescricao("Descrição da Pauta de Teste");

        Voto voto = new Voto();
        voto.setPautaId(pauta.getId());

        assertNotNull(voto.getPautaId());
        assertEquals("1", voto.getPautaId());
    }

    @Test
    void deveManterReferenciaCorretaComAssociado() {
        Associado associado = new Associado();
        associado.setId("1");
        associado.setNome("João");
        associado.setCpf("12345678909");

        Voto voto = new Voto();
        voto.setAssociadoId(associado.getId());

        assertNotNull(voto.getAssociadoId());
        assertEquals("1", voto.getAssociadoId());
    }
}