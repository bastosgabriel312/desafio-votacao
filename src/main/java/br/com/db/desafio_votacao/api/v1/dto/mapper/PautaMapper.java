package br.com.db.desafio_votacao.api.v1.dto.mapper;

import br.com.db.desafio_votacao.api.v1.dto.PautaDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.PautaResponseDTO;
import br.com.db.desafio_votacao.model.Pauta;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PautaMapper {
    PautaResponseDTO toDto(Pauta pauta);
    Pauta toEntity(PautaDTO pautaDTO);
    List<PautaResponseDTO> toDto(List<Pauta> pauta);
    List<Pauta> toEntity(List<PautaDTO> pautaDTOs);
}
