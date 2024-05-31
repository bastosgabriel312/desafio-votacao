package br.com.db.desafio_votacao.repository;

import br.com.db.desafio_votacao.enumeration.EnumSimNao;
import br.com.db.desafio_votacao.model.Associado;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.model.Voto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotoRepository extends CrudRepository<Voto, Long> {
    Page<Voto> findAll(Pageable pageable);

    @Query("SELECT COUNT(v) FROM Voto v " +
            "JOIN v.sessaoVotacao sv " +
            "JOIN sv.pauta p " +
            "WHERE p.id = :pautaId " +
            "AND v.voto = :votoSim")
    Long countVotosSimByPautaId(@Param("pautaId") Long pautaId, @Param("votoSim") EnumSimNao votoSim);

    @Query("SELECT COUNT(v) FROM Voto v " +
            "JOIN v.sessaoVotacao sv " +
            "JOIN sv.pauta p " +
            "WHERE p.id = :pautaId " +
            "AND v.voto = :votoNao")
    Long countVotosNaoByPautaId(@Param("pautaId") Long pautaId, @Param("votoNao") EnumSimNao votoNao);

    @Query("SELECT v FROM Voto v " +
            "JOIN v.sessaoVotacao sv " +
            "JOIN sv.pauta p " +
                    "WHERE p.id = :pautaId " +
                    "AND v.associadoId = :associadoId")
    Optional<Voto> findVotoByAssociadoAndPauta(@Param("pautaId") Long pautaId, @Param("associadoId") Long associadoId);

}