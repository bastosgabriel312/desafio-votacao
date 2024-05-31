package br.com.db.desafio_votacao.v1.service;

import br.com.db.desafio_votacao.api.v1.dto.VotoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.VotoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.VotoResponseDTO;
import br.com.db.desafio_votacao.exception.ResourceNotFoundException;
import br.com.db.desafio_votacao.exception.SessaoEncerradaException;
import br.com.db.desafio_votacao.exception.VotoJaRegistradoException;
import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import br.com.db.desafio_votacao.model.Voto;
import br.com.db.desafio_votacao.repository.AssociadoRepository;
import br.com.db.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.db.desafio_votacao.repository.VotoRepository;
import br.com.db.desafio_votacao.service.VotoServiceV1Impl;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private VotoMapper votoMapper;

    @InjectMocks
    private VotoServiceV1Impl votoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Salvar com sessão e associado existentes deve salvar voto")
    void salvar_ComSessaoEAssociadoExistentes_DeveSalvarVoto() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setSessaoId(1L);
        votoDTO.setAssociadoId(1L);
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setAtiva(true);
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setId(1L);
        sessaoVotacao.setAtiva(true);
        sessaoVotacao.setPauta(pauta);
        Associado associado = new Associado();
        associado.setId(1L);

        Voto voto = new Voto();
        voto.setId(1L);

        when(sessaoVotacaoRepository.findById(anyLong())).thenReturn(Optional.of(sessaoVotacao));
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.of(associado));
        when(votoMapper.toEntity(any(VotoDTO.class))).thenReturn(voto);
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);
        when(votoMapper.toDto(any(Voto.class))).thenReturn(new VotoResponseDTO());

        VotoResponseDTO responseDTO = votoService.salvar(votoDTO);

        assertNotNull(responseDTO);
        verify(sessaoVotacaoRepository, times(1)).findById(anyLong());
        verify(associadoRepository, times(1)).findById(anyLong());
        verify(votoRepository, times(1)).save(any(Voto.class));
    }

    @Test
    @DisplayName("Salvar com sessão inexistente deve lançar ResourceNotFoundException")
    void salvar_ComSessaoInexistente_DeveLancarResourceNotFoundException() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setSessaoId(1L);

        when(sessaoVotacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> votoService.salvar(votoDTO));
        verify(sessaoVotacaoRepository, times(1)).findById(anyLong());
        verify(associadoRepository, times(0)).findById(anyLong());
        verify(votoRepository, times(0)).save(any(Voto.class));
    }

    @Test
    @DisplayName("Salvar com associado inexistente deve lançar ResourceNotFoundException")
    void salvar_ComAssociadoInexistente_DeveLancarResourceNotFoundException() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setSessaoId(1L);
        votoDTO.setAssociadoId(1L);

        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setId(1L);

        when(sessaoVotacaoRepository.findById(anyLong())).thenReturn(Optional.of(sessaoVotacao));
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> votoService.salvar(votoDTO));
        verify(sessaoVotacaoRepository, times(1)).findById(anyLong());
        verify(associadoRepository, times(1)).findById(anyLong());
        verify(votoRepository, times(0)).save(any(Voto.class));
    }

    @Test
    @DisplayName("Salvar com sessão encerrada deve lançar SessaoEncerradaException")
    void salvar_ComSessaoEncerrada_DeveLancarSessaoEncerradaException() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setSessaoId(1L);
        votoDTO.setAssociadoId(1L);

        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setId(1L);
        sessaoVotacao.setAtiva(false);

        Associado associado = new Associado();
        associado.setId(1L);

        when(sessaoVotacaoRepository.findById(anyLong())).thenReturn(Optional.of(sessaoVotacao));
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.of(associado));

        assertThrows(SessaoEncerradaException.class, () -> votoService.salvar(votoDTO));
        verify(sessaoVotacaoRepository, times(1)).findById(anyLong());
        verify(associadoRepository, times(1)).findById(anyLong());
        verify(votoRepository, times(0)).save(any(Voto.class));
    }

    @Test
    @DisplayName("Salvar com voto já registrado deve lançar VotoJaRegistradoException")
    void salvar_ComVotoJaRegistrado_DeveLancarVotoJaRegistradoException() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setSessaoId(1L);
        votoDTO.setAssociadoId(1L);
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setAtiva(true);

        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setId(1L);
        sessaoVotacao.setAtiva(true);
        sessaoVotacao.setPauta(pauta);

        Associado associado = new Associado();
        associado.setId(1L);

        Voto voto = new Voto();
        voto.setId(1L);

        when(sessaoVotacaoRepository.findById(anyLong())).thenReturn(Optional.of(sessaoVotacao));
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.of(associado));
        when(votoRepository.findVotoByAssociadoAndPauta(anyLong(), anyLong())).thenReturn(Optional.of(voto));

        assertThrows(VotoJaRegistradoException.class, () -> votoService.salvar(votoDTO));
        verify(sessaoVotacaoRepository, times(1)).findById(anyLong());
        verify(associadoRepository, times(1)).findById(anyLong());
        verify(votoRepository, times(1)).findVotoByAssociadoAndPauta(anyLong(), anyLong());
        verify(votoRepository, times(0)).save(any(Voto.class));
    }

    @Test
    @DisplayName("Listar todos deve retornar página de votos")
    void listarTodos_DeveRetornarPaginaDeVotos() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Voto> page = new PageImpl<>(Collections.singletonList(new Voto()));

        when(votoRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<VotoResponseDTO> result = votoService.listarTodos(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(votoRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Buscar por ID deve retornar VotoResponseDTO")
    void buscarPorId_DeveRetornarVotoResponseDTO() {
        Voto voto = new Voto();
        voto.setId(1L);

        when(votoRepository.findById(anyLong())).thenReturn(Optional.of(voto));
        when(votoMapper.toDto(any(Voto.class))).thenReturn(new VotoResponseDTO());

        VotoResponseDTO result = votoService.buscarPorId(1L);

        assertNotNull(result);
        verify(votoRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Buscar por ID com ID inválido deve lançar ResourceNotFoundException")
    void buscarPorId_ComIdInvalido_DeveLancarResourceNotFoundException() {
        when(votoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> votoService.buscarPorId(1L));
        verify(votoRepository, times(1)).findById(anyLong());
    }
}

