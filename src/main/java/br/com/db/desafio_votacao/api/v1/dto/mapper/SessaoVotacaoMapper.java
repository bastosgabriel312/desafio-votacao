package br.com.db.desafio_votacao.api.v1.dto.mapper;

import br.com.db.desafio_votacao.api.v1.dto.SessaoVotacaoDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.SessaoVotacaoResponseDTO;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SessaoVotacaoMapper {
    SessaoVotacaoResponseDTO toDto(SessaoVotacao sessaoVotacao);
    SessaoVotacao toEntity(SessaoVotacaoDTO sessaoVotacaoDto);
    List<SessaoVotacaoResponseDTO> toDto(List<SessaoVotacao> sessoesVotacao);
    List<SessaoVotacaoDTO> toEntity(List<SessaoVotacaoDTO> sessaoVotacaoDTOs);
}
