package br.com.db.desafio_votacao.api.v1.controller;

import br.com.db.desafio_votacao.api.v1.dto.AssociadoDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.AssociadoResponseDTO;
import br.com.db.desafio_votacao.service.AssociadoServiceV1Impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/associados")
public class AssociadoController {

    private static final Logger logger = LoggerFactory.getLogger(AssociadoController.class);

    @Autowired
    private AssociadoServiceV1Impl associadoServiceV1;


    @PostMapping
    public ResponseEntity<AssociadoResponseDTO> criarAssociado(@RequestBody AssociadoDTO associadoDTO) {
        logger.debug("Criando um novo associado: {}", associadoDTO);
        return ResponseEntity.ok(associadoServiceV1.salvar(associadoDTO));
    }

    @GetMapping
    public ResponseEntity<Page<AssociadoResponseDTO>> listarTodos(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        logger.debug("Listando todos os associados");
        Page<AssociadoResponseDTO> associados = associadoServiceV1.listarPorPagina(page, size);
        return ResponseEntity.ok(associados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssociadoResponseDTO> buscarPorId(@PathVariable Long id) throws Exception {
        logger.debug("Buscando associado por ID: {}", id);
        AssociadoResponseDTO associadoResponseDTO = associadoServiceV1.buscarPorId(id);
        return ResponseEntity.ok(associadoResponseDTO);
    }
}