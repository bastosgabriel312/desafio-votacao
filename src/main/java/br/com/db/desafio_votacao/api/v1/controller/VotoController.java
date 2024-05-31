package br.com.db.desafio_votacao.api.v1.controller;

import br.com.db.desafio_votacao.api.v1.dto.VotoDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.VotoResponseDTO;
import br.com.db.desafio_votacao.service.VotoServiceV1Impl;
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
@RequestMapping("/api/v1/votos")
@OpenAPIDefinition
public class VotoController {

    private static final Logger logger = LoggerFactory.getLogger(VotoController.class);

    @Autowired
    @Qualifier("VotoServiceV1")
    private VotoServiceV1Impl votoServiceV1;

    @Operation(summary = "Registrar um voto")
    @PostMapping
    public ResponseEntity<VotoResponseDTO> registrarVoto(@Valid @RequestBody VotoDTO votoDTO) {
        return ResponseEntity.ok(votoServiceV1.salvar(votoDTO));
    }

    @Operation(summary = "Listar todos os votos")
    @ApiResponse(responseCode = "200", description = "Retorna a lista de todos os votos")
    @GetMapping
    public ResponseEntity<Page<VotoResponseDTO>> listarTodos(
            @Parameter(description = "Número da página (começa do zero)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", in = ParameterIn.QUERY)
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        logger.debug("Listando todos os votos");
        return ResponseEntity.ok(votoServiceV1.listarTodos(PageRequest.of(page,size)));
    }

    @Operation(summary = "Buscar voto por ID")
    @ApiResponse(responseCode = "200", description = "Retorna o voto encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<VotoResponseDTO> buscarPorId(
            @Parameter(description = "ID do voto a ser buscado", in = ParameterIn.PATH)
            @PathVariable Long id) {
        logger.debug("Buscando voto por ID: {}", id);
        return ResponseEntity.ok(votoServiceV1.buscarPorId(id));
    }
}
