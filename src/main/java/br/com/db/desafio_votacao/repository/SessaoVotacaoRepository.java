package br.com.db.desafio_votacao.repository;

import br.com.db.desafio_votacao.model.SessaoVotacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessaoVotacaoRepository extends CrudRepository<SessaoVotacao, Long> {

    List<SessaoVotacao> findAllByDataFimBeforeAndAtiva(LocalDateTime dataFim, boolean ativa);
    Page<SessaoVotacao> findAll(Pageable pageable);
}