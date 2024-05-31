package br.com.db.desafio_votacao.api.v1.controller;

import br.com.db.desafio_votacao.api.v1.dto.PautaDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.PautaResponseDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.PautaMapper;
import br.com.db.desafio_votacao.service.PautaServiceV1Impl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas")
@OpenAPIDefinition
public class PautaController {

    private static final Logger logger = LoggerFactory.getLogger(PautaController.class);

    @Autowired
    private PautaServiceV1Impl pautaServiceV1;

    @Autowired
    private PautaMapper pautaMapper;

    @Operation(summary = "Cadastrar uma nova pauta")
    @PostMapping
    public ResponseEntity<PautaResponseDTO> criarPauta(@Valid @RequestBody PautaDTO pautaDTO) {
        return ResponseEntity.ok(pautaServiceV1.salvar(pautaDTO));
    }

    @Operation(summary = "Listar todas as pautas")
    @ApiResponse(responseCode = "200", description = "Retorna a lista de pautas")
    @GetMapping
    public ResponseEntity<Page<PautaResponseDTO>> listarTodos(
            @Parameter(description = "Número da página (começa do zero)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", in = ParameterIn.QUERY)
            @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        logger.debug("Listando todas as pautas");
        Page<PautaResponseDTO> pautas = pautaServiceV1.listarTodos(PageRequest.of(page,size));
        return ResponseEntity.ok(pautas);
    }

    @Operation(summary = "Buscar pauta por ID")
    @ApiResponse(responseCode = "200", description = "Retorna a pauta encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<PautaResponseDTO> buscarPorId(
            @Parameter(description = "ID da pauta a ser buscada", in = ParameterIn.PATH)
            @PathVariable Long id) {
        logger.debug("Buscando pauta por ID: {}", id);
        PautaResponseDTO pauta = pautaServiceV1.buscarPorId(id);
        if (pauta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pauta);
    }
}
