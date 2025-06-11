package com.cooperativa.votacao.infrastructure.rest;

import com.cooperativa.votacao.application.dto.RegistrarVotoRequest;
import com.cooperativa.votacao.application.service.VotoService;
import com.cooperativa.votacao.domain.model.Voto;
import com.cooperativa.votacao.infrastructure.exception.VotoJaRegistradoException;
import com.cooperativa.votacao.infrastructure.exception.SessaoNaoAbertaException;
import com.cooperativa.votacao.infrastructure.exception.PautaNaoEncontradaException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VotoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VotoService votoService;

    @InjectMocks
    private VotoController votoController;

    private ObjectMapper objectMapper;

    private final String PAUTA_ID = "1";
    private final String ASSOCIADO_ID = "123";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(votoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private Voto criarVotoMock() {
        Voto voto = new Voto();
        voto.setId("1");
        voto.setPautaId(PAUTA_ID);
        voto.setAssociadoId(ASSOCIADO_ID);
        voto.setVotoFavoravel(true);
        return voto;
    }

    @Test
    void deveRegistrarVotoComSucesso() throws Exception {
        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);
        Voto votoSalvo = criarVotoMock();

        when(votoService.registrarVoto(any(RegistrarVotoRequest.class))).thenReturn(votoSalvo);

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.pautaId").value(PAUTA_ID))
                .andExpect(jsonPath("$.associadoId").value(ASSOCIADO_ID))
                .andExpect(jsonPath("$.votoFavoravel").value(true));
    }

    @Test
    void deveRetornarConflictQuandoAssociadoJaVotou() throws Exception {
        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);

        when(votoService.registrarVoto(any(RegistrarVotoRequest.class)))
                .thenThrow(new VotoJaRegistradoException("Associado já votou nesta pauta"));

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Associado já votou nesta pauta"));
    }

    @Test
    void deveRetornarUnprocessableEntityQuandoSessaoFechada() throws Exception {
        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);

        when(votoService.registrarVoto(any(RegistrarVotoRequest.class)))
                .thenThrow(new SessaoNaoAbertaException("Sessão de votação não está aberta"));

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Sessão de votação não está aberta"));
    }

    @Test
    void deveRetornarNotFoundQuandoPautaNaoEncontrada() throws Exception {
        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);

        when(votoService.registrarVoto(any(RegistrarVotoRequest.class)))
                .thenThrow(new PautaNaoEncontradaException("Pauta não encontrada"));

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pauta não encontrada"));
    }

    @Test
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        String requestInvalido = "{\"pautaId\": null, \"associadoId\": null, \"votoFavoravel\": null}";

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarResultadoVotacao() throws Exception {
        VotoService.ResultadoVotacao resultado = new VotoService.ResultadoVotacao(PAUTA_ID, 2L, 1L);
        when(votoService.obterResultado(PAUTA_ID)).thenReturn(resultado);

        mockMvc.perform(get("/api/votos/resultado/" + PAUTA_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pautaId").value(PAUTA_ID))
                .andExpect(jsonPath("$.votosSim").value(2))
                .andExpect(jsonPath("$.votosNao").value(1));
    }
}