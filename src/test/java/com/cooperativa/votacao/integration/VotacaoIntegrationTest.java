//package com.cooperativa.votacao.integration;
//
//import com.cooperativa.votacao.application.dto.RegistrarVotoRequest;
//import com.cooperativa.votacao.application.service.VotoService;
//import com.cooperativa.votacao.domain.model.Associado;
//import com.cooperativa.votacao.domain.model.Pauta;
//import com.cooperativa.votacao.domain.model.Voto;
//import com.cooperativa.votacao.domain.ports.PautaRepository;
//import com.cooperativa.votacao.domain.ports.UserInfoPort;
//import com.cooperativa.votacao.domain.ports.VotoRepository;
//import com.cooperativa.votacao.domain.repository.AssociadoRepository;
//import com.github.tomakehurst.wiremock.WireMockServer;
//import com.github.tomakehurst.wiremock.client.WireMock;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class VotacaoIntegrationTest {
//
//    @Autowired
//    private PautaRepository pautaRepository;
//
//    @Autowired
//    private VotoRepository votoRepository;
//
//    @Autowired
//    private AssociadoRepository associadoRepository;
//
//    @Autowired
//    private VotoService votoService;
//
//    @Autowired
//    private UserInfoPort userInfoClient;
//
//    private WireMockServer wireMockServer;
//    private final String CPF_VALIDO = "12345678909";
//    private final Long ASSOCIADO_ID = 1L;
//
//    @BeforeEach
//    void setup() {
//        wireMockServer = new WireMockServer(8089);
//        wireMockServer.start();
//        WireMock.configureFor("localhost", 8089);
//
//
//        votoRepository.deleteAll();
//        pautaRepository.deleteAll();
//        associadoRepository.deleteAll();
//    }
//
//    @AfterEach
//    void tearDown() {
//        wireMockServer.stop();
//    }
//
//    @Test
//    void devePermitirVotacaoQuandoAssociadoHabilitado() {
//
//        stubFor(get(urlEqualTo("/users/" + CPF_VALIDO)).willReturn(aResponse().withStatus(200).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).withBody("{\"status\": \"ABLE_TO_VOTE\"}")));
//
//        Associado associado = criarAssociado();
//        Pauta pauta = pautaRepository.save(criarPautaAberta());
//
//        Voto voto = votoService.registrarVoto(new RegistrarVotoRequest(pauta.getId(), associado.getId(), true));
//
//
//        assertNotNull(voto);
//        assertNotNull(voto.getId());
//        assertTrue(voto.isVotoFavoravel());
//        assertEquals(pauta.getId(), voto.getPauta().getId());
//        assertEquals(associado.getId(), voto.getAssociado().getId());
//
//
//        assertTrue(votoRepository.existsByPautaIdAndAssociado_Id(pauta.getId(), associado.getId()));
//    }
//
//    @Test
//    void deveContabilizarVotosCorretamente() {
//
//        Associado associado1 = criarAssociado();
//        Associado associado2 = criarAssociado();
//        Associado associado3 = criarAssociado();
//        Pauta pauta = pautaRepository.save(criarPautaAberta());
//
//
//        Voto voto1 = new Voto();
//        voto1.setPauta(pauta);
//        voto1.setAssociado(associado1);
//        voto1.setVotoFavoravel(true);
//        votoRepository.save(voto1);
//
//        Voto voto2 = new Voto();
//        voto2.setPauta(pauta);
//        voto2.setAssociado(associado2);
//        voto2.setVotoFavoravel(false);
//        votoRepository.save(voto2);
//
//        Voto voto3 = new Voto();
//        voto3.setPauta(pauta);
//        voto3.setAssociado(associado3);
//        voto3.setVotoFavoravel(true);
//        votoRepository.save(voto3);
//
//
//        VotoService.ResultadoVotacao resultado = votoService.obterResultado(pauta.getId());
//
//
//        assertNotNull(resultado);
//        assertEquals(pauta.getId(), resultado.pautaId());
//        assertEquals(2L, resultado.votosSim());
//        assertEquals(1L, resultado.votosNao());
//        assertEquals(3L, resultado.votosSim() + resultado.votosNao());
//    }
//
//    @Test
//    void deveLancarExcecaoAoVotarEmPautaFechada() {
//
//        Associado associado = criarAssociado();
//        final Pauta pauta = pautaRepository.save(criarPautaFechada());
//
//
//        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
//            votoService.registrarVoto(new RegistrarVotoRequest(pauta.getId(), associado.getId(), true));
//        });
//
//        assertEquals("Sessão de votação não está aberta", exception.getMessage());
//        assertFalse(votoRepository.existsByPautaIdAndAssociado_Id(pauta.getId(), associado.getId()));
//    }
//
//    private Pauta criarPautaAberta() {
//        LocalDateTime agora = LocalDateTime.now();
//        Pauta pauta = new Pauta();
//        pauta.setTitulo("Pauta de Teste");
//        pauta.setDescricao("Descrição da Pauta de Teste");
//        pauta.setDataCriacao(agora);
//        pauta.setSessaoAberta(true);
//        pauta.setInicioSessao(agora.minusMinutes(1));
//        pauta.setFimSessao(agora.plusMinutes(4));
//        return pauta;
//    }
//
//    private Pauta criarPautaFechada() {
//        LocalDateTime agora = LocalDateTime.now();
//        Pauta pauta = new Pauta();
//        pauta.setTitulo("Pauta Fechada");
//        pauta.setDescricao("Descrição da Pauta Fechada");
//        pauta.setDataCriacao(agora.minusDays(1));
//        pauta.setSessaoAberta(false);
//        pauta.setInicioSessao(agora.minusMinutes(10));
//        pauta.setFimSessao(agora.minusMinutes(5));
//        return pauta;
//    }
//
//    private Associado criarAssociado() {
//        Associado associado = new Associado();
//        associado.setNome("Associado Teste");
//        associado.setCpf(CPF_VALIDO);
//        return associadoRepository.save(associado);
//    }
//}