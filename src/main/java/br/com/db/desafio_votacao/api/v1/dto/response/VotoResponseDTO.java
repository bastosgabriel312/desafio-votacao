package br.com.db.desafio_votacao.api.v1.dto.response;

import br.com.db.desafio_votacao.api.v1.dto.mapper.AssociadoMapper;
import br.com.db.desafio_votacao.api.v1.dto.mapper.SessaoVotacaoMapper;
import br.com.db.desafio_votacao.enumeration.EnumSimNao;
import br.com.db.desafio_votacao.model.Voto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class VotoResponseDTO {
    private Long id;
    private SessaoVotacaoResponseDTO sessao;
    private Long associadoId;
    private EnumSimNao voto;
}

