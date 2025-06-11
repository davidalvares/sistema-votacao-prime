package com.cooperativa.votacao.infrastructure.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cooperativa.votacao.application.dto.AssociadoRequest;
import com.cooperativa.votacao.application.service.AssociadoService;
import com.cooperativa.votacao.domain.model.Associado;
import com.cooperativa.votacao.infrastructure.exception.AssociadoJaExisteException;
import com.cooperativa.votacao.infrastructure.exception.AssociadoNaoEncontradoException;
import com.cooperativa.votacao.infrastructure.exception.CpfInvalidoException;
import com.cooperativa.votacao.infrastructure.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class AssociadoControllerTest {

    private static final String ASSOCIADO_ID = "1";
    private static final String NOME = "João da Silva";
    private static final String CPF = "12345678909";

    @Mock
    private AssociadoService associadoService;

    @InjectMocks
    private AssociadoController associadoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(associadoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deveCadastrarAssociadoComSucesso() throws Exception {
        AssociadoRequest request = new AssociadoRequest(NOME, CPF);
        Associado associado = new Associado();
        associado.setId(ASSOCIADO_ID);
        associado.setNome(NOME);
        associado.setCpf(CPF);

        when(associadoService.cadastrarAssociado(any(AssociadoRequest.class))).thenReturn(associado);

        mockMvc.perform(post("/api/associados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ASSOCIADO_ID))
                .andExpect(jsonPath("$.nome").value(NOME))
                .andExpect(jsonPath("$.cpf").value(CPF));
    }

    @Test
    void deveRetornarConflictQuandoCpfJaExiste() throws Exception {
        AssociadoRequest request = new AssociadoRequest(NOME, CPF);

        when(associadoService.cadastrarAssociado(any(AssociadoRequest.class)))
                .thenThrow(new AssociadoJaExisteException("CPF já cadastrado"));

        mockMvc.perform(post("/api/associados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("CPF já cadastrado"));
    }

    @Test
    void deveRetornarUnprocessableEntityQuandoCpfInvalido() throws Exception {
        AssociadoRequest request = new AssociadoRequest(NOME, CPF);

        when(associadoService.cadastrarAssociado(any(AssociadoRequest.class)))
                .thenThrow(new CpfInvalidoException("CPF inválido"));

        mockMvc.perform(post("/api/associados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("CPF inválido"));
    }

    @Test
    void deveBuscarAssociadoPorId() throws Exception {
        Associado associado = new Associado();
        associado.setId(ASSOCIADO_ID);
        associado.setNome(NOME);
        associado.setCpf(CPF);

        when(associadoService.buscarPorId(ASSOCIADO_ID)).thenReturn(associado);

        mockMvc.perform(get("/api/associados/{id}", ASSOCIADO_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ASSOCIADO_ID))
                .andExpect(jsonPath("$.nome").value(NOME))
                .andExpect(jsonPath("$.cpf").value(CPF));
    }

    @Test
    void deveRetornarNotFoundQuandoAssociadoNaoEncontrado() throws Exception {
        when(associadoService.buscarPorId(ASSOCIADO_ID))
                .thenThrow(new AssociadoNaoEncontradoException("Associado não encontrado"));

        mockMvc.perform(get("/api/associados/{id}", ASSOCIADO_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Associado não encontrado"));
    }

    @Test
    void deveListarTodosAssociados() throws Exception {
        Associado associado1 = new Associado();
        associado1.setId("1");
        associado1.setNome("João");
        associado1.setCpf("12345678909");

        Associado associado2 = new Associado();
        associado2.setId("2");
        associado2.setNome("Maria");
        associado2.setCpf("98765432100");

        List<Associado> associados = Arrays.asList(associado1, associado2);

        when(associadoService.findAll()).thenReturn(associados);

        mockMvc.perform(get("/api/associados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].nome").value("João"))
                .andExpect(jsonPath("$[0].cpf").value("12345678909"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].nome").value("Maria"))
                .andExpect(jsonPath("$[1].cpf").value("98765432100"));
    }

    @Test
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        String requestInvalido = "{\"nome\": \"\", \"cpf\": \"\"}";

        mockMvc.perform(post("/api/associados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestInvalido))
                .andExpect(status().isBadRequest());
    }
}