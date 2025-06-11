package com.cooperativa.votacao.infrastructure.rest;

import com.cooperativa.votacao.application.service.PautaService;
import com.cooperativa.votacao.infrastructure.exception.PautaNaoEncontradaException;
import com.cooperativa.votacao.infrastructure.exception.SessaoJaAbertaException;
import com.cooperativa.votacao.domain.model.Pauta;
import com.cooperativa.votacao.infrastructure.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PautaControllerTest {

    private static final LocalDateTime DATA_FIXA = LocalDateTime.of(2024, 1, 1, 10, 0);
    private static final String PAUTA_ID = "1";

    @Mock
    private PautaService pautaService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @InjectMocks
    private PautaController pautaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pautaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveCriarPautaComSucesso() throws Exception {
        String titulo = "Título da Pauta";
        String descricao = "Descrição da Pauta";
        PautaController.CriarPautaRequest request = new PautaController.CriarPautaRequest(titulo, descricao);

        Pauta pautaEsperada = new Pauta();
        pautaEsperada.setId(PAUTA_ID);
        pautaEsperada.setTitulo(titulo);
        pautaEsperada.setDescricao(descricao);
        pautaEsperada.setDataCriacao(DATA_FIXA);
        pautaEsperada.setSessaoAberta(false);

        when(pautaService.criar(eq(titulo), eq(descricao))).thenReturn(pautaEsperada);

        mockMvc.perform(post("/api/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(PAUTA_ID))
                .andExpect(jsonPath("$.titulo").value(titulo))
                .andExpect(jsonPath("$.descricao").value(descricao))
                .andExpect(jsonPath("$.sessaoAberta").value(false));
    }

    @Test
    void deveRetornarBadRequestQuandoDadosInvalidosNaCriacao() throws Exception {
        PautaController.CriarPautaRequest request = new PautaController.CriarPautaRequest("", "");

        mockMvc.perform(post("/api/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAbrirSessaoComSucesso() throws Exception {
        int duracaoMinutos = 5;
        PautaController.AbrirSessaoRequest request = new PautaController.AbrirSessaoRequest(duracaoMinutos);

        Pauta pautaEsperada = new Pauta();
        pautaEsperada.setId(PAUTA_ID);
        pautaEsperada.setSessaoAberta(false);
        pautaEsperada.setInicioSessao(DATA_FIXA);
        pautaEsperada.setFimSessao(DATA_FIXA.plusMinutes(duracaoMinutos));

        when(pautaService.abrirSessao(eq(PAUTA_ID), eq(duracaoMinutos))).thenReturn(pautaEsperada);

        mockMvc.perform(post("/api/pautas/{id}/abrir-sessao", PAUTA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PAUTA_ID))
                .andExpect(jsonPath("$.sessaoAberta").value(false));
    }

    @Test
    void deveRetornarConflictQuandoSessaoJaAberta() throws Exception {
        int duracaoMinutos = 5;
        PautaController.AbrirSessaoRequest request = new PautaController.AbrirSessaoRequest(duracaoMinutos);

        when(pautaService.abrirSessao(eq(PAUTA_ID), eq(duracaoMinutos)))
                .thenThrow(new SessaoJaAbertaException("Sessão já está aberta"));

        mockMvc.perform(post("/api/pautas/{id}/abrir-sessao", PAUTA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Sessão já está aberta"));
    }

    @Test
    void deveRetornarNotFoundQuandoPautaNaoEncontrada() throws Exception {
        int duracaoMinutos = 5;
        PautaController.AbrirSessaoRequest request = new PautaController.AbrirSessaoRequest(duracaoMinutos);

        when(pautaService.abrirSessao(eq(PAUTA_ID), eq(duracaoMinutos)))
                .thenThrow(new PautaNaoEncontradaException("Pauta não encontrada"));

        mockMvc.perform(post("/api/pautas/{id}/abrir-sessao", PAUTA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pauta não encontrada"));
    }

    @Test
    void deveListarTodasPautas() throws Exception {
        Pauta pauta1 = new Pauta();
        pauta1.setId("1");
        pauta1.setTitulo("Pauta 1");
        pauta1.setSessaoAberta(false);

        Pauta pauta2 = new Pauta();
        pauta2.setId("2");
        pauta2.setTitulo("Pauta 2");
        pauta2.setSessaoAberta(false);

        List<Pauta> pautas = Arrays.asList(pauta1, pauta2);

        when(pautaService.findAll()).thenReturn(pautas);

        mockMvc.perform(get("/api/pautas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].titulo").value("Pauta 1"))
                .andExpect(jsonPath("$[0].sessaoAberta").value(false))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].titulo").value("Pauta 2"))
                .andExpect(jsonPath("$[1].sessaoAberta").value(false));
    }

    @Test
    void deveBuscarPautaPorId() throws Exception {
        Pauta pauta = new Pauta();
        pauta.setId(PAUTA_ID);
        pauta.setTitulo("Pauta de Teste");
        pauta.setDescricao("Descrição da Pauta de Teste");
        pauta.setSessaoAberta(false);

        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(pauta);

        mockMvc.perform(get("/api/pautas/{id}", PAUTA_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PAUTA_ID))
                .andExpect(jsonPath("$.titulo").value("Pauta de Teste"))
                .andExpect(jsonPath("$.descricao").value("Descrição da Pauta de Teste"))
                .andExpect(jsonPath("$.sessaoAberta").value(false));
    }

    @Test
    void deveRetornarNotFoundAoBuscarPautaInexistente() throws Exception {
        when(pautaService.buscarPorId(PAUTA_ID))
                .thenThrow(new PautaNaoEncontradaException("Pauta não encontrada"));

        mockMvc.perform(get("/api/pautas/{id}", PAUTA_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pauta não encontrada"));
    }
}