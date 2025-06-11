package com.cooperativa.votacao.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AssociadoTest {

    @Test
    void deveCriarAssociadoComSucesso() {
        String nome = "João da Silva";
        String cpf = "12345678909";

        Associado associado = new Associado();
        associado.setNome(nome);
        associado.setCpf(cpf);

        assertNotNull(associado);
        assertEquals(nome, associado.getNome());
        assertEquals(cpf, associado.getCpf());
    }

    @Test
    void deveCriarAssociadoUsandoConstrutorAllArgs() {
        String id = "1";
        String nome = "João da Silva";
        String cpf = "12345678909";

        Associado associado = new Associado();
        associado.setId(id);
        associado.setNome(nome);
        associado.setCpf(cpf);

        assertNotNull(associado);
        assertEquals(id, associado.getId());
        assertEquals(nome, associado.getNome());
        assertEquals(cpf, associado.getCpf());
    }

    @Test
    void deveCompararAssociadosCorretamente() {
        Associado associado1 = new Associado();
        associado1.setId("1");
        associado1.setNome("João");
        associado1.setCpf("12345678909");

        Associado associado2 = new Associado();
        associado2.setId("1");
        associado2.setNome("João");
        associado2.setCpf("12345678909");

        Associado associado3 = new Associado();
        associado3.setId("2");
        associado3.setNome("Maria");
        associado3.setCpf("98765432100");

        assertEquals(associado1, associado2);
        assertNotEquals(associado1, associado3);
    }
}