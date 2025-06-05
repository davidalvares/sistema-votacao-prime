//package com.cooperativa.votacao.infrastructure.rest;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.cooperativa.votacao.application.dto.AssociadoRequest;
//import com.cooperativa.votacao.application.service.AssociadoService;
//import com.cooperativa.votacao.domain.model.Associado;
//import com.cooperativa.votacao.infrastructure.exception.CpfInvalidoException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@WebMvcTest(AssociadoController.class)
//class AssociadoControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private AssociadoService associadoService;
//
//    @Test
//    void deveCadastrarAssociadoComSucesso() throws Exception {
//
//        AssociadoRequest request = new AssociadoRequest("João da Silva", "12345678900");
//        Associado associado = new Associado(1L, "João da Silva", "12345678900");
//        when(associadoService.cadastrarAssociado(any(AssociadoRequest.class))).thenReturn(associado);
//
//
//        mockMvc.perform(post("/api/associados")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id").value(associado.getId()))
//            .andExpect(jsonPath("$.nome").value(associado.getNome()))
//            .andExpect(jsonPath("$.cpf").value(associado.getCpf()));
//    }
//
//    @Test
//    void deveRetornarBadRequestQuandoCpfInvalido() throws Exception {
//
//        AssociadoRequest request = new AssociadoRequest("João da Silva", "12345678900");
//        when(associadoService.cadastrarAssociado(any(AssociadoRequest.class)))
//            .thenThrow(new CpfInvalidoException("CPF não está habilitado para votar"));
//
//
//        mockMvc.perform(post("/api/associados")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isBadRequest())
//            .andExpect(jsonPath("$.message").value("CPF não está habilitado para votar"));
//    }
//
//    @Test
//    void deveRetornarBadRequestQuandoCpfJaCadastrado() throws Exception {
//
//        AssociadoRequest request = new AssociadoRequest("João da Silva", "12345678900");
//        when(associadoService.cadastrarAssociado(any(AssociadoRequest.class)))
//            .thenThrow(new IllegalArgumentException("CPF já cadastrado"));
//
//
//        mockMvc.perform(post("/api/associados")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isBadRequest())
//            .andExpect(jsonPath("$.message").value("CPF já cadastrado"));
//    }
//
//    @Test
//    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
//
//        AssociadoRequest request = new AssociadoRequest("", "");
//
//
//        mockMvc.perform(post("/api/associados")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isBadRequest());
//    }
//}