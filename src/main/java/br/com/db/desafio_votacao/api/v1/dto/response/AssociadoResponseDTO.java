package br.com.db.desafio_votacao.api.v1.dto.response;

import br.com.db.desafio_votacao.model.Associado;
import lombok.Data;

@Data
public class AssociadoResponseDTO {
    private Long id;
    private String nome;
    private String cpf;

}
