package br.com.db.desafio_votacao.api.v1.dto.response;

import lombok.Data;

@Data
public class PautaResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
}