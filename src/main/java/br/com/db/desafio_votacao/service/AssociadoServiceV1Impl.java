package br.com.db.desafio_votacao.service;

import br.com.db.desafio_votacao.api.v1.dto.AssociadoDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.AssociadoMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.AssociadoResponseDTO;
import br.com.db.desafio_votacao.client.CpfValidationClient;
import br.com.db.desafio_votacao.exception.InvalidCpfException;
import br.com.db.desafio_votacao.exception.ResourceNotFoundException;
import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.repository.AssociadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AssociadoServiceV1Impl implements AssociadoService {

    private static final Logger logger = LoggerFactory.getLogger(AssociadoServiceV1Impl.class);

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private AssociadoMapper associadoMapper;

    @Autowired
    private CpfValidationClient cpfValidationClient;

    @Override
    public AssociadoResponseDTO salvar(AssociadoDTO associadoDTO) {
        // Verifica a validade do CPF
        if (!cpfValidationClient.isCpfValid(associadoDTO.getCpf())) {
            throw new InvalidCpfException("CPF inválido: " + associadoDTO.getCpf());
        }
        Associado associado = associadoMapper.toEntity(associadoDTO);
        Associado associadoSalvo = associadoRepository.save(associado);
        logger.debug("Criando um novo associado: {}", associadoDTO);
        return associadoMapper.toDto(associadoSalvo);
    }

    @Override
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

    @Override
    public String verificarElegibilidadeVotacao(String cpf) {
        String status = cpfValidationClient.checkVotingEligibility(cpf);
        if (status.equals("INVALID_CPF")) {
            throw new InvalidCpfException("CPF inválido: " + cpf);
        }
        return status;
    }

}
