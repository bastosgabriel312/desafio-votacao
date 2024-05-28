package br.com.db.desafio_votacao.repository;

import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.model.Voto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends CrudRepository<Voto, Long> {
    Page<Voto> findAll(Pageable pageable);
}