package br.com.db.desafio_votacao.service;

import br.com.db.desafio_votacao.api.v1.controller.AssociadoController;
import br.com.db.desafio_votacao.api.v1.dto.SessaoVotacaoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.SessaoVotacaoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.SessaoVotacaoResponseDTO;
import br.com.db.desafio_votacao.exception.ResourceNotFoundException;
import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import br.com.db.desafio_votacao.repository.PautaRepository;
import br.com.db.desafio_votacao.repository.SessaoVotacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("SessaoVotacaoServiceV1")
public class SessaoVotacaoServiceV1Impl implements SessaoVotacaoService {

    private static final Logger logger = LoggerFactory.getLogger(AssociadoController.class);


    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private SessaoVotacaoMapper sessaoVotacaoMapper;

    @Autowired
    private PautaRepository pautaRepository;

    @Scheduled(fixedRate = 1000) // Executa a cada segundo
    @Override
    public void verificarSessoes() {
        List<SessaoVotacao> sessoes = sessaoVotacaoRepository.findAllByDataFimBeforeAndAtiva(LocalDateTime.now(), true);
        sessoes.forEach(sessaoVotacao -> {
            sessaoVotacao.setAtiva(false);
            sessaoVotacaoRepository.save(sessaoVotacao);
            logger.debug("Encerrando sessão: {}", sessaoVotacao);
        });
    }

    public SessaoVotacaoResponseDTO abrirSessao(SessaoVotacaoDTO sessaoVotacaoDTO) {
        Pauta pauta = pautaRepository.findById(sessaoVotacaoDTO.getPautaId()).orElseThrow(()-> new ResourceNotFoundException(Pauta.class));
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setDataInicio(LocalDateTime.now());
        sessaoVotacao.setDataFim(LocalDateTime.now().plusMinutes(1));
        sessaoVotacao.setAtiva(true);
        sessaoVotacao.setPauta(pauta);
        SessaoVotacao sessaoVotacaoSalva = sessaoVotacaoRepository.save(sessaoVotacao);
        return sessaoVotacaoMapper.toDto(sessaoVotacaoSalva);
    }

    public Page<SessaoVotacaoResponseDTO> listarTodosPorPagina(Pageable pageable) {
        return sessaoVotacaoRepository.findAll(pageable).map(sessaoVotacaoMapper::toDto);
    }

    public SessaoVotacaoResponseDTO buscarPorId(Long id) {
        return sessaoVotacaoRepository.findById(id).map(sessaoVotacaoMapper::toDto).orElseThrow(()-> new ResourceNotFoundException(SessaoVotacao.class));
    }


}
