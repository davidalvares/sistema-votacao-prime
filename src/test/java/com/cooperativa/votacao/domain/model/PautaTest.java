package com.cooperativa.votacao.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PautaTest {

    @Test
    void deveCriarPautaComSucesso() {
        // given
        Pauta pauta = new Pauta();
        pauta.setTitulo("Título da Pauta");
        pauta.setDescricao("Descrição da Pauta");
        pauta.setDataCriacao(LocalDateTime.now());

        // then
        assertNotNull(pauta);
        assertEquals("Título da Pauta", pauta.getTitulo());
        assertEquals("Descrição da Pauta", pauta.getDescricao());
        assertNotNull(pauta.getDataCriacao());
        assertFalse(pauta.isSessaoAberta());
    }

    @Test
    void deveManipularSessaoCorretamente() {
        // given
        Pauta pauta = new Pauta();
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioSessao = agora.minusMinutes(1);
        LocalDateTime fimSessao = agora.plusMinutes(1);

        // when
        pauta.setInicioSessao(inicioSessao);
        pauta.setFimSessao(fimSessao);
        pauta.setSessaoAberta(true);

        // then
        assertEquals(inicioSessao, pauta.getInicioSessao());
        assertEquals(fimSessao, pauta.getFimSessao());
        assertTrue(pauta.isSessaoAberta());
    }

    @Test
    void deveVerificarSessaoAbertaCorretamente() {
        // given
        Pauta pauta = new Pauta();
        LocalDateTime agora = LocalDateTime.now();
        
        // when - sessão não iniciada
        pauta.setSessaoAberta(false);
        assertFalse(pauta.isSessaoAberta());
        
        // when - sessão aberta e dentro do prazo
        pauta.setSessaoAberta(true);
        pauta.setInicioSessao(agora.minusMinutes(1));
        pauta.setFimSessao(agora.plusMinutes(1));
        assertTrue(pauta.isSessaoAberta());
        
        // when - sessão aberta mas fora do prazo
        pauta.setInicioSessao(agora.minusMinutes(3));
        pauta.setFimSessao(agora.minusMinutes(1));
        assertFalse(pauta.isSessaoAberta());
    }
} 