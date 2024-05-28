package br.com.db.desafio_votacao.api.v1.dto;

import br.com.db.desafio_votacao.enumeration.EnumSimNao;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Data;

@Data
public class VotoDTO {
    private Long sessaoId;
    private Long associadoId;
    private EnumSimNao voto;
}