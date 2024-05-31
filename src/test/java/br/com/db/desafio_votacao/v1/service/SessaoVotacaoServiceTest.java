package br.com.db.desafio_votacao.v1.service;

import br.com.db.desafio_votacao.api.v1.dto.SessaoVotacaoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.SessaoVotacaoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.SessaoVotacaoResponseDTO;
import br.com.db.desafio_votacao.exception.ResourceNotFoundException;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import br.com.db.desafio_votacao.repository.PautaRepository;
import br.com.db.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.db.desafio_votacao.service.PautaServiceV1Impl;
import br.com.db.desafio_votacao.service.SessaoVotacaoServiceV1Impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SessaoVotacaoServiceTest {

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoVotacaoMapper sessaoVotacaoMapper;

    @Mock
    private PautaServiceV1Impl pautaServiceV1;

    @InjectMocks
    private SessaoVotacaoServiceV1Impl sessaoVotacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Abrir sessão com pauta existente deve abrir sessão")
    void abrirSessao_ComPautaExistente_DeveAbrirSessao() {
        SessaoVotacaoDTO sessaoVotacaoDTO = new SessaoVotacaoDTO();
        sessaoVotacaoDTO.setPautaId(1L);
        sessaoVotacaoDTO.setTempoEmSegundos(600L);

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Título Teste");
        pauta.setDescricao("Descrição Teste");

        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setPauta(pauta);
        sessaoVotacao.setDataInicio(LocalDateTime.now());
        sessaoVotacao.setDataFim(LocalDateTime.now().plusSeconds(600));
        sessaoVotacao.setAtiva(true);

        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoVotacao);
        when(sessaoVotacaoMapper.toDto(any(SessaoVotacao.class))).thenReturn(new SessaoVotacaoResponseDTO());

        SessaoVotacaoResponseDTO responseDTO = sessaoVotacaoService.abrirSessao(sessaoVotacaoDTO);

        assertNotNull(responseDTO);
        verify(pautaRepository, times(1)).findById(anyLong());
        verify(sessaoVotacaoRepository, times(1)).save(any(SessaoVotacao.class));
    }

    @Test
    @DisplayName("Abrir sessão com pauta inexistente deve lançar ResourceNotFoundException")
    void abrirSessao_ComPautaInexistente_DeveLancarResourceNotFoundException() {
        SessaoVotacaoDTO sessaoVotacaoDTO = new SessaoVotacaoDTO();
        sessaoVotacaoDTO.setPautaId(1L);

        when(pautaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sessaoVotacaoService.abrirSessao(sessaoVotacaoDTO));
        verify(pautaRepository, times(1)).findById(anyLong());
        verify(sessaoVotacaoRepository, times(0)).save(any(SessaoVotacao.class));
    }

    @Test
    @DisplayName("Listar todos por página deve retornar página de sessões")
    void listarTodosPorPagina_DeveRetornarPaginaDeSessoes() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<SessaoVotacao> page = new PageImpl<>(Collections.singletonList(new SessaoVotacao()));

        when(sessaoVotacaoRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<SessaoVotacaoResponseDTO> result = sessaoVotacaoService.listarTodosPorPagina(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(sessaoVotacaoRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Buscar por ID deve retornar SessaoVotacaoResponseDTO")
    void buscarPorId_DeveRetornarSessaoVotacaoResponseDTO() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setId(1L);

        when(sessaoVotacaoRepository.findById(anyLong())).thenReturn(Optional.of(sessaoVotacao));
        when(sessaoVotacaoMapper.toDto(any(SessaoVotacao.class))).thenReturn(new SessaoVotacaoResponseDTO());

        SessaoVotacaoResponseDTO result = sessaoVotacaoService.buscarPorId(1L);

        assertNotNull(result);
        verify(sessaoVotacaoRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Buscar por ID com ID inválido deve lançar ResourceNotFoundException")
    void buscarPorId_ComIdInvalido_DeveLancarResourceNotFoundException() {
        when(sessaoVotacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sessaoVotacaoService.buscarPorId(1L));
        verify(sessaoVotacaoRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Verificar sessões deve encerrar sessões vencidas")
    void verificarSessoes_DeveEncerrarSessoesVencidas() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setId(1L);
        sessaoVotacao.setAtiva(true);

        when(sessaoVotacaoRepository.findAllByDataFimBeforeAndAtiva(any(LocalDateTime.class), eq(true)))
                .thenReturn(List.of(sessaoVotacao));
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoVotacao);

        sessaoVotacaoService.verificarSessoes();

        assertFalse(sessaoVotacao.isAtiva());
        verify(sessaoVotacaoRepository, times(1)).findAllByDataFimBeforeAndAtiva(any(LocalDateTime.class), eq(true));
        verify(sessaoVotacaoRepository, times(1)).save(any(SessaoVotacao.class));
        verify(pautaServiceV1, times(1)).encerrarSessaoVotacao(any(SessaoVotacao.class));
    }
}
