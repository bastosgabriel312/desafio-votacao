package br.com.db.desafio_votacao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String descricao;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(nullable = false)
    private boolean ativa = true;

    @Column(nullable = false)
    private Long quantidadeVotosSim = 0L;

    @Column(nullable = false)
    private Long quantidadeVotosNao = 0L;

    /**
     * Incrementa o número de votos 'Sim'.
     */
    public void incrementarVotosSim() {
        this.quantidadeVotosSim++;
    }

    /**
     * Incrementa o número de votos 'Não'.
     */
    public void incrementarVotosNao() {
        this.quantidadeVotosNao++;
    }
}
