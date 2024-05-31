package br.com.db.desafio_votacao.service;

import br.com.db.desafio_votacao.api.v1.dto.VotoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.VotoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.VotoResponseDTO;
import br.com.db.desafio_votacao.exception.ResourceNotFoundException;
import br.com.db.desafio_votacao.exception.SessaoEncerradaException;
import br.com.db.desafio_votacao.exception.VotoJaRegistradoException;
import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import br.com.db.desafio_votacao.model.Voto;
import br.com.db.desafio_votacao.repository.AssociadoRepository;
import br.com.db.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.db.desafio_votacao.repository.VotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("VotoServiceV1")
public class VotoServiceV1Impl implements VotoService{

    private static final Logger logger = LoggerFactory.getLogger(VotoServiceV1Impl.class);

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private VotoMapper votoMapper;

    public VotoResponseDTO salvar(VotoDTO votoDTO) {
        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findById(votoDTO.getSessaoId()).orElseThrow(()-> new ResourceNotFoundException(SessaoVotacao.class));
        Associado associado = associadoRepository.findById(votoDTO.getAssociadoId()).orElseThrow(()-> new ResourceNotFoundException(Associado.class));
        validaSessaoVoto(sessaoVotacao,associado);
        Voto voto = votoMapper.toEntity(votoDTO);
        voto.setSessaoVotacao(sessaoVotacao);
        Voto votoSalvo = votoRepository.save(voto);

        logger.debug("Registrando um novo voto: {}", votoMapper.toDto(votoSalvo) );
        return votoMapper.toDto(votoSalvo);
    }

    public void validaSessaoVoto(SessaoVotacao sessaoVotacao, Associado associado) {
        // Verifica se a sessao esta ativa
        if(!sessaoVotacao.isAtiva()) { throw new SessaoEncerradaException("A sessão já foi encerrada");};
        // Verifica se o voto já foi registrado
        votoRepository.findVotoByAssociadoAndPauta(sessaoVotacao.getPauta().getId(), associado.getId()).ifPresent(
                voto -> {
                    throw new VotoJaRegistradoException("O associado já votou nesta pauta.");
                });
    }

    public Page<VotoResponseDTO> listarTodos(Pageable pageable) {
        Page<Voto> votos = votoRepository.findAll(pageable);
        return votos.map(votoMapper::toDto);
    }

    public VotoResponseDTO buscarPorId(Long id) {
        return votoRepository.findById(id).map(votoMapper::toDto).orElseThrow(()-> new ResourceNotFoundException(Voto.class));
    }
}
