package br.com.db.desafio_votacao.model;

import br.com.db.desafio_votacao.enumeration.EnumSimNao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    private SessaoVotacao sessaoVotacao;

    @Column(nullable = false)
    private Long associadoId;

    @Enumerated(EnumType.STRING)
    private EnumSimNao voto;
}
