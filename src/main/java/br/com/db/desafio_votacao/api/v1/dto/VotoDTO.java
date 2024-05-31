package br.com.db.desafio_votacao.api.v1.dto;

import br.com.db.desafio_votacao.enumeration.EnumSimNao;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VotoDTO {

    /**
     * ID da sessão de votação.
     */
    @NotNull(message = "ID da sessão é obrigatório")
    private Long sessaoId;

    /**
     * ID do associado que está votando.
     */
    @NotNull(message = "ID do associado é obrigatório")
    private Long associadoId;

    /**
     * Voto do associado (SIM ou NÃO).
     */
    @NotNull(message = "Voto é obrigatório")
    private EnumSimNao voto;
}