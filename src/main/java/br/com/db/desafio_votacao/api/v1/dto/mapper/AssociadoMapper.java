package br.com.db.desafio_votacao.api.v1.dto.mapper;

import br.com.db.desafio_votacao.api.v1.dto.response.AssociadoResponseDTO;
import br.com.db.desafio_votacao.api.v1.dto.AssociadoDTO;
import br.com.db.desafio_votacao.model.Associado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssociadoMapper {
    AssociadoResponseDTO toDto(Associado associado);
    @Mapping(target = "id", ignore = true)
    Associado toEntity(AssociadoDTO associadoDTO);
    List<AssociadoResponseDTO> toDto(List<Associado> associados);
    List<Associado> toEntity(List<AssociadoDTO> associadoDTOs);
}
