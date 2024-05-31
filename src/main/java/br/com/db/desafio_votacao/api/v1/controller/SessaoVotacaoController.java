package br.com.db.desafio_votacao.api.v1.controller;

import br.com.db.desafio_votacao.api.v1.dto.SessaoVotacaoDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.SessaoVotacaoResponseDTO;
import br.com.db.desafio_votacao.service.SessaoVotacaoServiceV1Impl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessoes")
@OpenAPIDefinition
public class SessaoVotacaoController {

    private static final Logger logger = LoggerFactory.getLogger(SessaoVotacaoController.class);

    @Autowired
    @Qualifier("SessaoVotacaoServiceV1")
    private SessaoVotacaoServiceV1Impl sessaoVotacaoServiceV1;

    @Operation(summary = "Abrir uma sessão de votação em uma pauta")
    @PostMapping
    public ResponseEntity<SessaoVotacaoResponseDTO> abrirSessao(@Valid @RequestBody SessaoVotacaoDTO sessaoVotacaoDTO) {
        return ResponseEntity.ok(sessaoVotacaoServiceV1.abrirSessao(sessaoVotacaoDTO));
    }

    @Operation(summary = "Listar todas as sessões de votação")
    @ApiResponse(responseCode = "200", description = "Retorna a lista de sessões de votação")
    @GetMapping
    public ResponseEntity<Page<SessaoVotacaoResponseDTO>> listarTodos(
            @Parameter(description = "Número da página (começa do zero)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", in = ParameterIn.QUERY)
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        logger.debug("Listando todas as sessões de votação");
        return ResponseEntity.ok(sessaoVotacaoServiceV1.listarTodosPorPagina(PageRequest.of(page,size)));
    }

    @Operation(summary = "Buscar sessão de votação por ID")
    @ApiResponse(responseCode = "200", description = "Retorna a sessão de votação encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<SessaoVotacaoResponseDTO> buscarPorId(
            @Parameter(description = "ID da sessão de votação a ser buscada", in = ParameterIn.PATH)
            @PathVariable Long id) {
        logger.debug("Buscando sessão de votação por ID: {}", id);
        return ResponseEntity.ok(sessaoVotacaoServiceV1.buscarPorId(id));
    }
}
