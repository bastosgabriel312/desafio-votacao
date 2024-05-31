package br.com.db.desafio_votacao.v1.service;

import br.com.db.desafio_votacao.api.v1.dto.PautaDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.PautaMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.PautaResponseDTO;
import br.com.db.desafio_votacao.enumeration.EnumSimNao;
import br.com.db.desafio_votacao.exception.ResourceNotFoundException;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import br.com.db.desafio_votacao.repository.PautaRepository;
import br.com.db.desafio_votacao.repository.VotoRepository;
import br.com.db.desafio_votacao.service.PautaServiceV1Impl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaMapper pautaMapper;

    @InjectMocks
    private PautaServiceV1Impl pautaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Salvar deve salvar pauta")
    void salvar_DeveSalvarPauta() {
        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setTitulo("Título Teste");
        pautaDTO.setDescricao("Descrição Teste");

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Título Teste");
        pauta.setDescricao("Descrição Teste");
        pauta.setDataCriacao(LocalDateTime.now());
        pauta.setAtiva(true);
        pauta.setQuantidadeVotosSim(0L);
        pauta.setQuantidadeVotosNao(0L);

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);
        when(pautaMapper.toDto(any(Pauta.class))).thenReturn(new PautaResponseDTO());

        PautaResponseDTO responseDTO = pautaService.salvar(pautaDTO);

        assertNotNull(responseDTO);
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    @DisplayName("Listar todos deve retornar página de pautas")
    void listarTodos_DeveRetornarPaginaDePautas() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Pauta> page = new PageImpl<>(Collections.singletonList(new Pauta()));

        when(pautaRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<PautaResponseDTO> result = pautaService.listarTodos(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(pautaRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Buscar por ID deve retornar PautaResponseDTO")
    void buscarPorId_DeveRetornarPautaResponseDTO() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Título Teste");
        pauta.setDescricao("Descrição Teste");

        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(pauta));
        when(pautaMapper.toDto(any(Pauta.class))).thenReturn(new PautaResponseDTO());

        PautaResponseDTO result = pautaService.buscarPorId(1L);

        assertNotNull(result);
        verify(pautaRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Buscar por ID com ID inválido deve lançar ResourceNotFoundException")
    void buscarPorId_ComIdInvalido_DeveLancarResourceNotFoundException() {
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pautaService.buscarPorId(1L));
        verify(pautaRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Encerrar sessão de votação deve contabilizar votos e atualizar pauta")
    void encerrarSessaoVotacao_DeveContabilizarVotosEAtualizarPauta() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Título Teste");
        pauta.setDescricao("Descrição Teste");

        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setPauta(pauta);

        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(pauta));
        when(votoRepository.countVotosSimByPautaId(anyLong(), any(EnumSimNao.class))).thenReturn(10L);
        when(votoRepository.countVotosNaoByPautaId(anyLong(), any(EnumSimNao.class))).thenReturn(5L);

        pautaService.encerrarSessaoVotacao(sessaoVotacao);

        assertEquals(10L, pauta.getQuantidadeVotosSim());
        assertEquals(5L, pauta.getQuantidadeVotosNao());
        assertFalse(pauta.isAtiva());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }
}

