package br.com.db.desafio_votacao.repository;

import br.com.db.desafio_votacao.model.Associado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends CrudRepository<Associado, Long> {

    Page<Associado> findAll(Pageable pageable);
}