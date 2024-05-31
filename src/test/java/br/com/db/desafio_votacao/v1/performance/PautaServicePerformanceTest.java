package br.com.db.desafio_votacao.v1.performance;

import br.com.db.desafio_votacao.api.v1.dto.PautaDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.PautaMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.PautaResponseDTO;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.repository.PautaRepository;
import br.com.db.desafio_votacao.service.PautaServiceV1Impl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@State(Scope.Benchmark)
public class PautaServicePerformanceTest {

    @InjectMocks
    private PautaServiceV1Impl pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @Autowired
    @Mock
    private PautaMapper pautaMapper;

    @Setup(Level.Trial)
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Teste");
        pauta.setDescricao("Descrição teste");
        pauta.setAtiva(true);
        pauta.setDataCriacao(LocalDateTime.now());

        when(pautaMapper.toEntity(any(PautaDTO.class))).thenReturn(pauta);
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);
        when(pautaMapper.toDto(any(Pauta.class))).thenReturn(new PautaResponseDTO());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSalvarPautaPerformance() {
        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setTitulo("Título Performance Test");
        pautaDTO.setDescricao("Descrição Performance Test");
        pautaService.salvar(pautaDTO);
    }
}
