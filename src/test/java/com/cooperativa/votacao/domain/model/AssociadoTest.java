//package com.cooperativa.votacao.domain.model;
//
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
//
//class AssociadoTest {
//
//    @Test
//    void deveCriarAssociadoComSucesso() {
//        // given
//        String nome = "João da Silva";
//        String cpf = "12345678909";
//
//        // when
//        Associado associado = new Associado();
//        associado.setNome(nome);
//        associado.setCpf(cpf);
//
//        // then
//        assertNotNull(associado);
//        assertEquals(nome, associado.getNome());
//        assertEquals(cpf, associado.getCpf());
//    }
//
//    @Test
//    void deveCriarAssociadoUsandoConstrutorAllArgs() {
//        // given
//        Long id = 1L;
//        String nome = "João da Silva";
//        String cpf = "12345678909";
//
//        // when
//        Associado associado = new Associado(id, nome, cpf);
//
//        // then
//        assertNotNull(associado);
//        assertEquals(id, associado.getId());
//        assertEquals(nome, associado.getNome());
//        assertEquals(cpf, associado.getCpf());
//    }
//
//    @Test
//    void deveCompararAssociadosCorretamente() {
//        // given
//        Associado associado1 = new Associado(1L, "João", "12345678909");
//        Associado associado2 = new Associado(1L, "João", "12345678909");
//        Associado associado3 = new Associado(2L, "Maria", "98765432100");
//
//        // then
//        assertEquals(associado1, associado2);
//        assertNotEquals(associado1, associado3);
//    }
//}