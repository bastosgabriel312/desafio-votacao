package br.com.db.desafio_votacao.api.v1.dto.mapper;

import br.com.db.desafio_votacao.api.v1.dto.VotoDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.VotoResponseDTO;
import br.com.db.desafio_votacao.model.Voto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VotoMapper {
    @Mapping(source = "sessaoVotacao", target = "sessao")
    VotoResponseDTO toDto(Voto Voto);
    Voto toEntity(VotoDTO VotoDTO);
    List<VotoResponseDTO> toDto(List<Voto> voto);
    List<Voto> toEntity(List<VotoDTO> votosDTOs);
}
