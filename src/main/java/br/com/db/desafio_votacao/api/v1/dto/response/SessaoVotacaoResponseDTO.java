package br.com.db.desafio_votacao.api.v1.dto.response;

import br.com.db.desafio_votacao.model.Pauta;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessaoVotacaoResponseDTO {
    private Long id;
    private PautaResponseDTO pauta;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private boolean ativa;
}
