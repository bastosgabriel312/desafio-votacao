package br.com.db.desafio_votacao.service;

import br.com.db.desafio_votacao.api.v1.dto.PautaDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.PautaMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.PautaResponseDTO;
import br.com.db.desafio_votacao.exception.ResourceNotFoundException;
import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PautaServiceV1Impl implements PautaService {
    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private PautaMapper pautaMapper;

    public PautaResponseDTO salvar(PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(pautaDTO.getTitulo());
        pauta.setDescricao(pautaDTO.getDescricao());
        pauta.setDataCriacao(LocalDateTime.now());
        Pauta pautaSalva = pautaRepository.save(pauta);
        return pautaMapper.toDto(pautaSalva);
    }

    public Page<PautaResponseDTO> listarTodos(Pageable pageable) {
        Page<Pauta> pautas = pautaRepository.findAll(pageable);
        return pautas.map(pautaMapper::toDto);
    }

    public PautaResponseDTO buscarPorId(Long id) {
        return pautaRepository.findById(id).map(pautaMapper::toDto).orElseThrow(()-> new ResourceNotFoundException(Pauta.class));
    }
}
