package br.com.db.desafio_votacao.service;

import br.com.db.desafio_votacao.api.v1.dto.AssociadoDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.AssociadoResponseDTO;

public interface AssociadoService {
    public AssociadoResponseDTO buscarPorId(Long id) throws Exception;

    public AssociadoResponseDTO salvar(AssociadoDTO associadoDTO);
}
