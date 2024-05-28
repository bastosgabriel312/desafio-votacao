package br.com.db.desafio_votacao.api.v1.controller;

import br.com.db.desafio_votacao.api.v1.dto.SessaoVotacaoDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.SessaoVotacaoResponseDTO;
import br.com.db.desafio_votacao.model.SessaoVotacao;
import br.com.db.desafio_votacao.service.SessaoVotacaoServiceV1Impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessoes")
public class SessaoVotacaoController {

    private static final Logger logger = LoggerFactory.getLogger(SessaoVotacaoController.class);

    @Autowired
    @Qualifier("SessaoVotacaoServiceV1")
    private SessaoVotacaoServiceV1Impl sessaoVotacaoServiceV1;

    @PostMapping
    public ResponseEntity<SessaoVotacaoResponseDTO> abrirSessao(@RequestBody SessaoVotacaoDTO sessaoVotacaoDTO) {
        logger.debug("Abrindo uma nova sessão de votação: {}", sessaoVotacaoDTO);
        return ResponseEntity.ok(sessaoVotacaoServiceV1.abrirSessao(sessaoVotacaoDTO));
    }

    @GetMapping
    public ResponseEntity<Page<SessaoVotacaoResponseDTO>> listarTodos(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        logger.debug("Listando todas as sessões de votação");
        return ResponseEntity.ok(sessaoVotacaoServiceV1.listarTodosPorPagina(PageRequest.of(page,size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoVotacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.debug("Buscando sessão de votação por ID: {}", id);
        return ResponseEntity.ok(sessaoVotacaoServiceV1.buscarPorId(id));
    }
}