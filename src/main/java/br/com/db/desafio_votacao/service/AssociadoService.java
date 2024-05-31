package br.com.db.desafio_votacao.service;

import br.com.db.desafio_votacao.api.v1.dto.AssociadoDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.AssociadoResponseDTO;
import org.springframework.data.domain.Page;

public interface AssociadoService {
    public AssociadoResponseDTO buscarPorId(Long id) throws Exception;

    public AssociadoResponseDTO salvar(AssociadoDTO associadoDTO);

    Page<AssociadoResponseDTO> listarPorPagina(int page, int size);


    String verificarElegibilidadeVotacao(String cpf);
}
