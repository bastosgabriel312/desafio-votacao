package br.com.db.desafio_votacao.v1.performance;

import br.com.db.desafio_votacao.api.v1.dto.AssociadoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.AssociadoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.AssociadoResponseDTO;
import br.com.db.desafio_votacao.client.CpfValidationClientFake;
import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.repository.AssociadoRepository;
import br.com.db.desafio_votacao.service.AssociadoServiceV1Impl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@State(Scope.Benchmark)
public class AssociadoServicePerformanceTest {

    @InjectMocks
    private AssociadoServiceV1Impl associadoService;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private AssociadoMapper associadoMapper;

    @Mock
    private CpfValidationClientFake cpfValidationClientFake;


    @Setup(Level.Trial)
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Associado associado = new Associado();
        associado.setId(1L);
        associado.setNome("Teste");
        associado.setCpf("12345678901");

        when(cpfValidationClientFake.isCpfValid(anyString())).thenReturn(true);
        when(associadoMapper.toEntity(any(AssociadoDTO.class))).thenReturn(associado);
        when(associadoRepository.save(any(Associado.class))).thenReturn(associado);
        when(associadoMapper.toDto(any(Associado.class))).thenReturn(new AssociadoResponseDTO());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSalvarAssociadoPerformance() {
        AssociadoDTO associadoDTO = new AssociadoDTO();
        associadoDTO.setNome("Teste");
        associadoDTO.setCpf("12345678901");
        associadoService.salvar(associadoDTO);
    }


}
