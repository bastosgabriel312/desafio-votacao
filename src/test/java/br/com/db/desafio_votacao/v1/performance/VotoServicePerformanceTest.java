package br.com.db.desafio_votacao.v1.performance;

import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.repository.AssociadoRepository;
import br.com.db.desafio_votacao.repository.PautaRepository;
import br.com.db.desafio_votacao.api.v1.dto.VotoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.VotoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.VotoResponseDTO;
import br.com.db.desafio_votacao.enumeration.EnumSimNao;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import br.com.db.desafio_votacao.model.Voto;
import br.com.db.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.db.desafio_votacao.repository.VotoRepository;
import br.com.db.desafio_votacao.service.VotoServiceV1Impl;
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
public class VotoServicePerformanceTest {

    @InjectMocks
    private VotoServiceV1Impl votoService;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Autowired
    @Mock
    private VotoMapper votoMapper;


    @Setup(Level.Trial)
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Associado associado = new Associado();
        associado.setId(1L);
        associado.setNome("Teste");
        associado.setCpf("12345678901");

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
        sessaoVotacao.setDataFim(LocalDateTime.now().plusMinutes(10));
        sessaoVotacao.setAtiva(true);

        Voto voto = new Voto();
        voto.setId(1L);
        voto.setSessaoVotacao(sessaoVotacao);
        voto.setAssociadoId(1L);

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoVotacao);
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoVotacao));
        when(associadoRepository.findById(any(Long.class))).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            Associado foundAssociado = new Associado();
            foundAssociado.setId(id);
            foundAssociado.setNome("Teste");
            foundAssociado.setCpf("1234567890" + id);
            return Optional.of(foundAssociado);
        });
        when(votoMapper.toEntity(any(VotoDTO.class))).thenReturn(voto);
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);
        when(votoMapper.toDto(any(Voto.class))).thenReturn(new VotoResponseDTO());
    }

    @Test
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSalvarVotoPerformance() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setSessaoId(1L);
        votoDTO.setAssociadoId(1L);
        votoDTO.setVoto(EnumSimNao.SIM);
        votoService.salvar(votoDTO);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSalvarMilVotosSim() {
        Long associadoId = 1L;
        for (int i = 0; i < 1000; i++) {
            VotoDTO votoDTO = new VotoDTO();
            votoDTO.setSessaoId(1L);
            votoDTO.setAssociadoId(associadoId++);
            votoDTO.setVoto(EnumSimNao.SIM);
            votoService.salvar(votoDTO);
        }
    }

    @Test
    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSalvarMilVotosNao() {
        Long associadoId = 1L;
        for (int i = 0; i < 1000; i++) {
            VotoDTO votoDTO = new VotoDTO();
            votoDTO.setSessaoId(1L);
            votoDTO.setAssociadoId(associadoId++);
            votoDTO.setVoto(EnumSimNao.NAO);
            votoService.salvar(votoDTO);
        }
    }
}
