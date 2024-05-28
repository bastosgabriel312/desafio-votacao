package br.com.db.desafio_votacao.service;

import br.com.db.desafio_votacao.api.v1.dto.AssociadoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.AssociadoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.AssociadoResponseDTO;
import br.com.db.desafio_votacao.exception.ResourceNotFoundException;
import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.repository.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AssociadoServiceV1Impl implements AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private AssociadoMapper associadoMapper;

    @Override
    public AssociadoResponseDTO salvar(AssociadoDTO associadoDTO) {
        Associado associado = new Associado(null, associadoDTO.getNome(), associadoDTO.getCpf());
        Associado associadoSalvo = associadoRepository.save(associado);
        return associadoMapper.toDto(associadoSalvo);
    }

    public Page<AssociadoResponseDTO> listarPorPagina(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "cpf");
        Page<Associado> associados = associadoRepository.findAll(pageRequest);
        return associados.map(associadoMapper::toDto);
    }

    @Override
    public AssociadoResponseDTO buscarPorId(Long id) throws Exception {
        return associadoRepository.findById(id).map(associadoMapper::toDto).orElseThrow(()-> new ResourceNotFoundException(Associado.class));
    }

}
