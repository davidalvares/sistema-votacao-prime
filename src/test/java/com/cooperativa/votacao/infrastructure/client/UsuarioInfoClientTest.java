package com.cooperativa.votacao.infrastructure.client;


import com.cooperativa.votacao.domain.exception.CpfInvalidoException;
import com.cooperativa.votacao.domain.model.StatusUsuario;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioInfoClientTest {

    private WireMockServer wireMockServer;
    private UsuarioInfoClientImpl userInfoClient;
    private final String CPF_VALIDO = "12345678909";
    private final String CPF_INVALIDO = "11111111111";

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
        userInfoClient = new UsuarioInfoClientImpl("http://localhost:8089", new RestTemplate());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void deveRetornarAbleToVoteQuandoCpfValido() {
        // given
        stubFor(get(urlEqualTo("/users/" + CPF_VALIDO))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"status\": \"ABLE_TO_VOTE\"}")));

        // when
        StatusUsuario status = userInfoClient.verificarCPF(CPF_VALIDO);

        // then
        assertEquals(StatusUsuario.ABLE_TO_VOTE, status);
        verify(getRequestedFor(urlEqualTo("/users/" + CPF_VALIDO)));
    }

    @Test
    void deveRetornarUnableToVoteQuandoCpfBloqueado() {
        // given
        stubFor(get(urlEqualTo("/users/" + CPF_VALIDO))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"status\": \"UNABLE_TO_VOTE\"}")));

        // when
        StatusUsuario status = userInfoClient.verificarCPF(CPF_VALIDO);

        // then
        assertEquals(StatusUsuario.UNABLE_TO_VOTE, status);
        verify(getRequestedFor(urlEqualTo("/users/" + CPF_VALIDO)));
    }

    @Test
    void deveLancarExcecaoQuandoCpfInvalido() {
        // given
        stubFor(get(urlEqualTo("/users/" + CPF_INVALIDO))
            .willReturn(aResponse()
                .withStatus(404)));

        // when/then
        assertThrows(CpfInvalidoException.class, () -> {
            userInfoClient.verificarCPF(CPF_INVALIDO);
        });
        verify(getRequestedFor(urlEqualTo("/users/" + CPF_INVALIDO)));
    }

    @Test
    void deveLancarExcecaoQuandoErroInesperado() {
        // given
        stubFor(get(urlEqualTo("/users/" + CPF_VALIDO))
            .willReturn(aResponse()
                .withStatus(500)));

        // when/then
        assertThrows(RuntimeException.class, () -> {
            userInfoClient.verificarCPF(CPF_VALIDO);
        });
        verify(getRequestedFor(urlEqualTo("/users/" + CPF_VALIDO)));
    }
} 