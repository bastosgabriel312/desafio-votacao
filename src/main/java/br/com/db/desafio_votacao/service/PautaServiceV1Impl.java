package br.com.db.desafio_votacao.service;

import br.com.db.desafio_votacao.api.v1.controller.PautaController;
import br.com.db.desafio_votacao.api.v1.dto.PautaDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.PautaMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.PautaResponseDTO;
import br.com.db.desafio_votacao.enumeration.EnumSimNao;
import br.com.db.desafio_votacao.exception.ResourceNotFoundException;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import br.com.db.desafio_votacao.repository.PautaRepository;
import br.com.db.desafio_votacao.repository.VotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PautaServiceV1Impl implements PautaService {

    private static final Logger logger = LoggerFactory.getLogger(PautaController.class);

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private PautaMapper pautaMapper;

    public PautaResponseDTO salvar(PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(pautaDTO.getTitulo());
        pauta.setDescricao(pautaDTO.getDescricao());
        pauta.setDataCriacao(LocalDateTime.now());
        pauta.setAtiva(true);
        pauta.setQuantidadeVotosSim(0L);
        pauta.setQuantidadeVotosNao(0L);
        Pauta pautaSalva = pautaRepository.save(pauta);
        logger.debug("Criando uma nova pauta: {}", pautaMapper.toDto(pautaSalva));
        return pautaMapper.toDto(pautaSalva);
    }

    public Page<PautaResponseDTO> listarTodos(Pageable pageable) {
        Page<Pauta> pautas = pautaRepository.findAll(pageable);
        return pautas.map(pautaMapper::toDto);
    }

    public PautaResponseDTO buscarPorId(Long id) {
        return pautaRepository.findById(id).map(pautaMapper::toDto).orElseThrow(() -> new ResourceNotFoundException(Pauta.class));
    }

    public void encerrarSessaoVotacao(SessaoVotacao sessaoVotacao) {
        Pauta pauta = pautaRepository.findById(sessaoVotacao.getPauta().getId()).orElseThrow(() -> new ResourceNotFoundException(Pauta.class));
        Pauta pautaEncerrada = contabilizarVotos(pauta);
        pautaRepository.save(pautaEncerrada);
    }

    public Pauta contabilizarVotos(Pauta pauta) {
        Long votosSim = votoRepository.countVotosSimByPautaId(pauta.getId(), EnumSimNao.SIM);
        Long votosNao = votoRepository.countVotosNaoByPautaId(pauta.getId(), EnumSimNao.NAO);
        pauta.setQuantidadeVotosSim(votosSim);
        pauta.setQuantidadeVotosNao(votosNao);
        pauta.setAtiva(false);
        return pauta;

    }
}
