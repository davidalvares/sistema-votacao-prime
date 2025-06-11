package com.cooperativa.votacao.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import com.cooperativa.votacao.application.dto.AssociadoRequest;
import com.cooperativa.votacao.domain.model.Associado;
import com.cooperativa.votacao.domain.repository.AssociadoRepository;
import com.cooperativa.votacao.infrastructure.client.UsuarioInfoClient;
import com.cooperativa.votacao.domain.model.StatusUsuario;
import com.cooperativa.votacao.infrastructure.exception.CpfInvalidoException;
import com.cooperativa.votacao.infrastructure.exception.AssociadoJaExisteException;
import com.cooperativa.votacao.infrastructure.exception.AssociadoNaoEncontradoException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AssociadoService")
class AssociadoServiceTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private UsuarioInfoClient usuarioInfoClient;

    @InjectMocks
    private AssociadoService associadoService;

    private AssociadoRequest validRequest;
    private Associado validAssociado;
    private static final String VALID_ID = "1";
    private static final String VALID_NAME = "João da Silva";
    private static final String VALID_CPF = "12345678900";

    @BeforeEach
    void setUp() {
        validRequest = new AssociadoRequest(VALID_NAME, VALID_CPF);
        validAssociado = new Associado();
        validAssociado.setId(VALID_ID);
        validAssociado.setNome(VALID_NAME);
        validAssociado.setCpf(VALID_CPF);
    }

    @Nested
    @DisplayName("Testes de cadastro de associado")
    class CadastroAssociadoTests {
        
        @Test
        @DisplayName("Deve cadastrar associado com sucesso quando dados são válidos")
        void deveCadastrarAssociadoComSucesso() {

            when(associadoRepository.existsByCpf(validRequest.cpf())).thenReturn(false);
            when(usuarioInfoClient.verificarCPF(validRequest.cpf())).thenReturn(StatusUsuario.ABLE_TO_VOTE);
            when(associadoRepository.save(any(Associado.class))).thenReturn(validAssociado);


            Associado resultado = associadoService.cadastrarAssociado(validRequest);


            assertNotNull(resultado);
            assertEquals(validAssociado.getId(), resultado.getId());
            assertEquals(validAssociado.getNome(), resultado.getNome());
            assertEquals(validAssociado.getCpf(), resultado.getCpf());

            ArgumentCaptor<Associado> associadoCaptor = ArgumentCaptor.forClass(Associado.class);
            verify(associadoRepository).save(associadoCaptor.capture());
            
            Associado associadoSalvo = associadoCaptor.getValue();
            assertEquals(validRequest.nome(), associadoSalvo.getNome());
            assertEquals(validRequest.cpf(), associadoSalvo.getCpf());
            
            verify(associadoRepository).existsByCpf(validRequest.cpf());
            verify(usuarioInfoClient).verificarCPF(validRequest.cpf());
        }

        @Test
        @DisplayName("Deve lançar exceção quando CPF já existe")
        void deveLancarExcecaoQuandoCpfJaExiste() {

            when(associadoRepository.existsByCpf(validRequest.cpf())).thenReturn(true);


            AssociadoJaExisteException exception = assertThrows(AssociadoJaExisteException.class, () -> 
                associadoService.cadastrarAssociado(validRequest)
            );
            assertEquals("CPF já cadastrado", exception.getMessage());

            verify(associadoRepository).existsByCpf(validRequest.cpf());
            verify(usuarioInfoClient, never()).verificarCPF(anyString());
            verify(associadoRepository, never()).save(any(Associado.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando CPF é inválido")
        void deveLancarExcecaoQuandoCpfInvalido() {

            when(associadoRepository.existsByCpf(validRequest.cpf())).thenReturn(false);
            when(usuarioInfoClient.verificarCPF(validRequest.cpf())).thenThrow(new CpfInvalidoException("CPF inválido"));


            CpfInvalidoException exception = assertThrows(CpfInvalidoException.class, () -> 
                associadoService.cadastrarAssociado(validRequest)
            );
            assertEquals("CPF inválido", exception.getMessage());

            verify(associadoRepository).existsByCpf(validRequest.cpf());
            verify(usuarioInfoClient).verificarCPF(validRequest.cpf());
            verify(associadoRepository, never()).save(any(Associado.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando associado não está habilitado para votar")
        void deveLancarExcecaoQuandoAssociadoNaoHabilitado() {

            when(associadoRepository.existsByCpf(validRequest.cpf())).thenReturn(false);
            when(usuarioInfoClient.verificarCPF(validRequest.cpf())).thenReturn(StatusUsuario.UNABLE_TO_VOTE);


            CpfInvalidoException exception = assertThrows(CpfInvalidoException.class, () -> 
                associadoService.cadastrarAssociado(validRequest)
            );
            assertEquals("CPF não está habilitado para votar", exception.getMessage());

            verify(associadoRepository).existsByCpf(validRequest.cpf());
            verify(usuarioInfoClient).verificarCPF(validRequest.cpf());
            verify(associadoRepository, never()).save(any(Associado.class));
        }
    }

    @Nested
    @DisplayName("Testes de busca de associado")
    class BuscaAssociadoTests {

        @Test
        @DisplayName("Deve buscar associado por ID com sucesso")
        void deveBuscarAssociadoPorId() {

            when(associadoRepository.findById(VALID_ID)).thenReturn(Optional.of(validAssociado));


            Associado resultado = associadoService.buscarPorId(VALID_ID);


            assertNotNull(resultado);
            assertEquals(VALID_ID, resultado.getId());
            assertEquals(VALID_NAME, resultado.getNome());
            assertEquals(VALID_CPF, resultado.getCpf());
            verify(associadoRepository).findById(VALID_ID);
        }

        @Test
        @DisplayName("Deve lançar exceção quando associado não é encontrado")
        void deveLancarExcecaoQuandoAssociadoNaoEncontrado() {

            String id = "999";
            when(associadoRepository.findById(id)).thenReturn(Optional.empty());


            AssociadoNaoEncontradoException exception = assertThrows(AssociadoNaoEncontradoException.class, () -> 
                associadoService.buscarPorId(id)
            );
            assertEquals("Associado não encontrado", exception.getMessage());
            verify(associadoRepository).findById(id);
        }

        @Test
        @DisplayName("Deve lançar exceção quando ID é nulo")
        void deveLancarExcecaoQuandoIdNulo() {

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                associadoService.buscarPorId(null)
            );
            assertEquals("ID do associado não pode ser nulo", exception.getMessage());
            verify(associadoRepository, never()).findById(any());
        }
    }

    @Nested
    @DisplayName("Testes de listagem de associados")
    class ListagemAssociadosTests {

        @Test
        @DisplayName("Deve listar todos os associados com sucesso")
        void deveListarTodosAssociados() {

            Associado associado1 = new Associado();
            associado1.setId("1");
            associado1.setNome("João");
            associado1.setCpf("12345678901");

            Associado associado2 = new Associado();
            associado2.setId("2");
            associado2.setNome("Maria");
            associado2.setCpf("12345678902");

            List<Associado> associados = Arrays.asList(associado1, associado2);
            when(associadoRepository.findAll()).thenReturn(associados);


            List<Associado> resultado = associadoService.findAll();


            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("João", resultado.get(0).getNome());
            assertEquals("Maria", resultado.get(1).getNome());
            verify(associadoRepository).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há associados cadastrados")
        void deveRetornarListaVaziaQuandoNaoHaAssociados() {

            when(associadoRepository.findAll()).thenReturn(List.of());


            List<Associado> resultado = associadoService.findAll();


            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(associadoRepository).findAll();
        }
    }
}