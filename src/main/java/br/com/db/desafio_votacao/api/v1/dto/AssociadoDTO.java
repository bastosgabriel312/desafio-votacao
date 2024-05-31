package br.com.db.desafio_votacao.api.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class AssociadoDTO {

    /**
     * Nome do associado.
     */
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    /**
     * CPF do associado.
     * Utilizada anotação brasileira para validação de CPF
     * Deve seguir o formato de CPF válido.
     */
    //@CPF(message = "CPF informado não existe") anotação comentada para utilização do facade client
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
    private String cpf;
}