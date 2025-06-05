//package com.cooperativa.votacao.application.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.cooperativa.votacao.application.dto.AssociadoRequest;
//import com.cooperativa.votacao.domain.model.Associado;
//import com.cooperativa.votacao.domain.repository.AssociadoRepository;
//import com.cooperativa.votacao.infrastructure.client.UsuarioInfoClient;
//import com.cooperativa.votacao.domain.model.StatusUsuario;
//import com.cooperativa.votacao.infrastructure.exception.CpfInvalidoException;
//
//@ExtendWith(MockitoExtension.class)
//class AssociadoServiceTest {
//
//    @Mock
//    private AssociadoRepository associadoRepository;
//
//    @Mock
//    private UsuarioInfoClient usuarioInfoClient;
//
//    @InjectMocks
//    private AssociadoService associadoService;
//
//    private AssociadoRequest validRequest;
//    private Associado validAssociado;
//
//    @BeforeEach
//    void setUp() {
//        validRequest = new AssociadoRequest("João da Silva", "12345678900");
//        validAssociado = new Associado(1L, "João da Silva", "12345678900");
//    }
//
//    @Test
//    void deveCadastrarAssociadoComSucesso() {
//
//        when(associadoRepository.existsByCpf(validRequest.cpf())).thenReturn(false);
//        when(usuarioInfoClient.verificarCPF(validRequest.cpf())).thenReturn(StatusUsuario.ABLE_TO_VOTE);
//        when(associadoRepository.save(any(Associado.class))).thenReturn(validAssociado);
//
//
//        Associado resultado = associadoService.cadastrarAssociado(validRequest);
//
//
//        assertNotNull(resultado);
//        assertEquals(validAssociado.getId(), resultado.getId());
//        assertEquals(validAssociado.getNome(), resultado.getNome());
//        assertEquals(validAssociado.getCpf(), resultado.getCpf());
//    }
//
//    @Test
//    void deveLancarExcecaoQuandoCpfJaCadastrado() {
//
//        when(associadoRepository.existsByCpf(validRequest.cpf())).thenReturn(true);
//
//
//        assertThrows(IllegalArgumentException.class,
//            () -> associadoService.cadastrarAssociado(validRequest),
//            "CPF já cadastrado");
//    }
//
//    @Test
//    void deveLancarExcecaoQuandoCpfNaoHabilitado() {
//
//        when(associadoRepository.existsByCpf(validRequest.cpf())).thenReturn(false);
//        when(usuarioInfoClient.verificarCPF(validRequest.cpf())).thenReturn(StatusUsuario.UNABLE_TO_VOTE);
//
//
//        assertThrows(CpfInvalidoException.class,
//            () -> associadoService.cadastrarAssociado(validRequest),
//            "CPF não está habilitado para votar");
//    }
//
//    @Test
//    void deveBuscarAssociadoPorId() {
//
//        when(associadoRepository.findById(validAssociado.getId())).thenReturn(java.util.Optional.of(validAssociado));
//
//
//        Associado resultado = associadoService.buscarPorId(validAssociado.getId());
//
//
//        assertNotNull(resultado);
//        assertEquals(validAssociado.getId(), resultado.getId());
//        assertEquals(validAssociado.getNome(), resultado.getNome());
//        assertEquals(validAssociado.getCpf(), resultado.getCpf());
//    }
//
//    @Test
//    void deveLancarExcecaoQuandoAssociadoNaoEncontrado() {
//
//        Long idInexistente = 999L;
//        when(associadoRepository.findById(idInexistente)).thenReturn(java.util.Optional.empty());
//
//
//        assertThrows(IllegalArgumentException.class,
//            () -> associadoService.buscarPorId(idInexistente),
//            "Associado não encontrado");
//    }
//}