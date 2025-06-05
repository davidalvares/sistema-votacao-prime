//package com.cooperativa.votacao.infrastructure.rest;
//
//import com.cooperativa.votacao.application.service.VotoService;
//import com.cooperativa.votacao.domain.model.Pauta;
//import com.cooperativa.votacao.domain.model.Voto;
//import com.cooperativa.votacao.domain.model.Associado;
//import com.cooperativa.votacao.application.dto.RegistrarVotoRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//
//@WebMvcTest(VotoController.class)
//@ExtendWith(MockitoExtension.class)
//class VotoControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private VotoService votoService;
//
//    private final Long PAUTA_ID = 1L;
//    private final Long ASSOCIADO_ID = 123L;
//
//    @Test
//    void deveRegistrarVotoComSucesso() throws Exception {
//
//        Pauta pauta = new Pauta();
//        pauta.setId(PAUTA_ID);
//
//        Associado associado = new Associado(ASSOCIADO_ID, "João", "12345678909");
//
//        Voto votoSalvo = new Voto();
//        votoSalvo.setId(1L);
//        votoSalvo.setPauta(pauta);
//        votoSalvo.setAssociado(associado);
//        votoSalvo.setVotoFavoravel(true);
//
//        RegistrarVotoRequest request = new RegistrarVotoRequest(PAUTA_ID, ASSOCIADO_ID, true);
//
//        when(votoService.registrarVoto(any(RegistrarVotoRequest.class))).thenReturn(votoSalvo);
//
//
//        mockMvc.perform(post("/api/votos")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id").value(1))
//            .andExpect(jsonPath("$.pauta.id").value(PAUTA_ID))
//            .andExpect(jsonPath("$.associado.id").value(ASSOCIADO_ID))
//            .andExpect(jsonPath("$.votoFavoravel").value(true));
//    }
//
//    @Test
//    void deveRetornarResultadoVotacao() throws Exception {
//
//        var resultado = new VotoService.ResultadoVotacao(PAUTA_ID, 2L, 1L);
//        when(votoService.obterResultado(PAUTA_ID)).thenReturn(resultado);
//
//
//        mockMvc.perform(get("/api/votos/resultado/" + PAUTA_ID))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.pautaId").value(PAUTA_ID))
//            .andExpect(jsonPath("$.votosSim").value(2))
//            .andExpect(jsonPath("$.votosNao").value(1));
//    }
//}