package br.com.db.desafio_votacao.api.v1.dto.response;

import br.com.db.desafio_votacao.enumeration.EnumSimNao;
import lombok.Data;

@Data
public class VotoResponseDTO {
    private Long id;
    private SessaoVotacaoResponseDTO sessao;
    private Long associadoId;
    private EnumSimNao voto;
}

