package br.com.db.desafio_votacao.v1.service;

import br.com.db.desafio_votacao.api.v1.dto.AssociadoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.AssociadoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.AssociadoResponseDTO;
import br.com.db.desafio_votacao.client.CpfValidationClient;
import br.com.db.desafio_votacao.exception.InvalidCpfException;
import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.repository.AssociadoRepository;
import br.com.db.desafio_votacao.service.AssociadoServiceV1Impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssociadoServiceTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private AssociadoMapper associadoMapper;

    @Mock
    private CpfValidationClient cpfValidationClient;

    @InjectMocks
    private AssociadoServiceV1Impl associadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Salvar associado com CPF válido deve salvar associado")
    void salvar_ComCpfValido_DeveSalvarAssociado() {
        AssociadoDTO associadoDTO = new AssociadoDTO();
        associadoDTO.setNome("Teste");
        associadoDTO.setCpf("12345678901");

        Associado associado = new Associado();
        associado.setId(1L);
        associado.setNome("Teste");
        associado.setCpf("12345678901");

        when(cpfValidationClient.isCpfValid(anyString())).thenReturn(true);
        when(associadoMapper.toEntity(any(AssociadoDTO.class))).thenReturn(associado);
        when(associadoRepository.save(any(Associado.class))).thenReturn(associado);
        when(associadoMapper.toDto(any(Associado.class))).thenReturn(new AssociadoResponseDTO());

        AssociadoResponseDTO responseDTO = associadoService.salvar(associadoDTO);

        assertNotNull(responseDTO);
        verify(cpfValidationClient, times(1)).isCpfValid(anyString());
        verify(associadoRepository, times(1)).save(any(Associado.class));
    }

    @Test
    @DisplayName("Salvar associado com CPF inválido deve lançar InvalidCpfException")
    void salvar_ComCpfInvalido_DeveLancarInvalidCpfException() {
        AssociadoDTO associadoDTO = new AssociadoDTO();
        associadoDTO.setNome("Teste");
        associadoDTO.setCpf("12345678901");

        when(cpfValidationClient.isCpfValid(anyString())).thenReturn(false);

        assertThrows(InvalidCpfException.class, () -> associadoService.salvar(associadoDTO));
        verify(cpfValidationClient, times(1)).isCpfValid(anyString());
        verify(associadoRepository, times(0)).save(any(Associado.class));
    }

    @Test
    @DisplayName("Listar todos deve retornar página de associados")
    void listarTodos_DeveRetornarPaginaDeAssociados() {
        Page<Associado> page = new PageImpl<>(Collections.singletonList(new Associado()));

        when(associadoRepository.findAll(any(PageRequest.class))).thenReturn(page);
        Page<AssociadoResponseDTO> result = associadoService.listarPorPagina(0,10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(associadoRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Buscar por ID deve retornar AssociadoResponseDTO")
    void buscarPorId_DeveRetornarAssociadoResponseDTO() throws Exception {
        Associado associado = new Associado();
        associado.setId(1L);
        associado.setNome("Teste");
        associado.setCpf("12345678901");

        when(associadoRepository.findById(anyLong())).thenReturn(Optional.of(associado));
        when(associadoMapper.toDto(any(Associado.class))).thenReturn(new AssociadoResponseDTO());

        AssociadoResponseDTO result = associadoService.buscarPorId(1L);

        assertNotNull(result);
        verify(associadoRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Verificar elegibilidade para votação deve retornar status de elegibilidade")
    void verificarElegibilidadeVotacao_DeveRetornarStatusElegibilidade() {
        String cpf = "12345678901";
        String status = "ABLE_TO_VOTE";

        when(cpfValidationClient.checkVotingEligibility(anyString())).thenReturn(status);

        String result = associadoService.verificarElegibilidadeVotacao(cpf);

        assertEquals(status, result);
        verify(cpfValidationClient, times(1)).checkVotingEligibility(anyString());
    }
}
