package br.com.db.desafio_votacao.v1.performance;

import br.com.db.desafio_votacao.repository.PautaRepository;
import br.com.db.desafio_votacao.service.PautaServiceV1Impl;
import br.com.db.desafio_votacao.api.v1.dto.PautaDTO;
import br.com.db.desafio_votacao.api.v1.dto.SessaoVotacaoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.PautaMapper;
import br.com.db.desafio_votacao.api.v1.dto.mapper.SessaoVotacaoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.SessaoVotacaoResponseDTO;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import br.com.db.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.db.desafio_votacao.service.SessaoVotacaoServiceV1Impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@State(Scope.Benchmark)
public class SessaoVotacaoServicePerformanceTest {

    @InjectMocks
    private SessaoVotacaoServiceV1Impl sessaoVotacaoService;

    @InjectMocks
    private PautaServiceV1Impl pautaServiceV1;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Autowired
    @Mock
    SessaoVotacaoMapper sessaoVotacaoMapper;

    @Autowired
    @Mock
    private PautaMapper pautaMapper;

    @Setup(Level.Trial)
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Teste");
        pauta.setDescricao("Descrição teste");
        pauta.setAtiva(true);
        pauta.setDataCriacao(LocalDateTime.now());

        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setId(1L);
        sessaoVotacao.setPauta(pauta);
        sessaoVotacao.setDataInicio(LocalDateTime.now());
        sessaoVotacao.setDataFim(LocalDateTime.now().plusMinutes(1));
        sessaoVotacao.setAtiva(true);

        when(sessaoVotacaoMapper.toEntity(any(SessaoVotacaoDTO.class))).thenReturn(sessaoVotacao);
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoVotacao);
        when(sessaoVotacaoMapper.toDto(any(SessaoVotacao.class))).thenReturn(new SessaoVotacaoResponseDTO());
    }

    @Test
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testAbrirSessaoPerformance() {
        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setTitulo("Teste");
        pautaDTO.setDescricao("Teste");
        SessaoVotacaoDTO sessaoVotacaoDTO = new SessaoVotacaoDTO();
        sessaoVotacaoDTO.setPautaId(1L);
        sessaoVotacaoDTO.setTempoEmSegundos(60L);
        sessaoVotacaoService.abrirSessao(sessaoVotacaoDTO);
    }
}
