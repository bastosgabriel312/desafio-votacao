package br.com.db.desafio_votacao.service;

import br.com.db.desafio_votacao.api.v1.dto.VotoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.VotoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.AssociadoResponseDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.VotoResponseDTO;
import br.com.db.desafio_votacao.exception.ResourceNotFoundException;
import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import br.com.db.desafio_votacao.model.Voto;
import br.com.db.desafio_votacao.repository.AssociadoRepository;
import br.com.db.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.db.desafio_votacao.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("VotoServiceV1")
public class VotoServiceV1Impl implements VotoService{
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
        if(sessaoVotacao == null) return null;
        Voto voto = new Voto();
        voto.setVoto(votoDTO.getVoto());
        voto.setAssociadoId(associado.getId());
        voto.setSessaoVotacao(sessaoVotacao);
        Voto votoSalvo = votoRepository.save(voto);
        return votoMapper.toDto(votoSalvo);
    }

    public Page<VotoResponseDTO> listarTodos(Pageable pageable) {
        Page<Voto> votos = votoRepository.findAll(pageable);
        return votos.map(votoMapper::toDto);
    }

    public VotoResponseDTO buscarPorId(Long id) {
        return votoRepository.findById(id).map(votoMapper::toDto).orElseThrow(()-> new ResourceNotFoundException(Voto.class));
    }
}
