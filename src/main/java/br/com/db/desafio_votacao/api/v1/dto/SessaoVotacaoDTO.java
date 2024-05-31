package br.com.db.desafio_votacao.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessaoVotacaoDTO {
    /**
     * ID da pauta associada à sessão de votação.
     */
    @NotNull(message = "O ID da pauta é obrigatório")
    private Long pautaId;

    /**
     * Tempo em segundos para a duração da sessão de votação.
     * Se não especificado, um valor padrão será utilizado.
     */
    @Min(value = 1, message = "O tempo em segundos deve ser maior que zero")
    private Long tempoEmSegundos;
}
