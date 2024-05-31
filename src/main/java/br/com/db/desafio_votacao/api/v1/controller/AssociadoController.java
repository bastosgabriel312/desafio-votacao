package br.com.db.desafio_votacao.api.v1.controller;

import br.com.db.desafio_votacao.api.v1.dto.AssociadoDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.AssociadoResponseDTO;
import br.com.db.desafio_votacao.service.AssociadoServiceV1Impl;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/associados")
@OpenAPIDefinition
public class AssociadoController {

    private static final Logger logger = LoggerFactory.getLogger(AssociadoController.class);

    @Autowired
    private AssociadoServiceV1Impl associadoServiceV1;


    @Operation(summary = "Cadastrar um novo associado")
    @PostMapping
    public ResponseEntity<AssociadoResponseDTO> criarAssociado(
            @Valid @RequestBody AssociadoDTO associadoDTO) {
        return ResponseEntity.ok(associadoServiceV1.salvar(associadoDTO));
    }

    @Operation(summary = "Listar todos os associados")
    @ApiResponse(responseCode = "200", description = "Retorna a lista de associados")
    @GetMapping
    public ResponseEntity<Page<AssociadoResponseDTO>> listarTodos(
            @Parameter(description = "Número da página (começa do zero)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", in = ParameterIn.QUERY)
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        logger.debug("Listando todos os associados");
        Page<AssociadoResponseDTO> associados = associadoServiceV1.listarPorPagina(page, size);
        return ResponseEntity.ok(associados);
    }

    @Operation(summary = "Buscar associado por ID")
    @ApiResponse(responseCode = "200", description = "Retorna o associado encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<AssociadoResponseDTO> buscarPorId(
            @Parameter(description = "ID do associado a ser buscado", in = ParameterIn.PATH)
            @PathVariable Long id) throws Exception {
        logger.debug("Buscando associado por ID: {}", id);
        AssociadoResponseDTO associadoResponseDTO = associadoServiceV1.buscarPorId(id);
        return ResponseEntity.ok(associadoResponseDTO);
    }
}
