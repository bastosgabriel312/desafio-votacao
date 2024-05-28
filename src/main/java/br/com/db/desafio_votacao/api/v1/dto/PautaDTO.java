package br.com.db.desafio_votacao.api.v1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PautaDTO {
    private String titulo;
    private String descricao;
}