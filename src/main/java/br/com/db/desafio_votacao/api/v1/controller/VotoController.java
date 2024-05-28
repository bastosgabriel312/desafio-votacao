package br.com.db.desafio_votacao.api.v1.controller;

import br.com.db.desafio_votacao.api.v1.dto.VotoDTO;
import br.com.db.desafio_votacao.api.v1.dto.response.VotoResponseDTO;
import br.com.db.desafio_votacao.service.VotoServiceV1Impl;
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
public class VotoController {

    private static final Logger logger = LoggerFactory.getLogger(VotoController.class);

    @Autowired
    @Qualifier("VotoServiceV1")
    private VotoServiceV1Impl votoServiceV1;

    @PostMapping
    public ResponseEntity<VotoResponseDTO> registrarVoto(@RequestBody VotoDTO votoDTO) {
        logger.debug("Registrando um novo voto: {}", votoDTO);
        return ResponseEntity.ok(votoServiceV1.salvar(votoDTO));
    }

    @GetMapping
    public ResponseEntity<Page<VotoResponseDTO>> listarTodos(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        logger.debug("Listando todos os votos");
        return ResponseEntity.ok(votoServiceV1.listarTodos(PageRequest.of(page,size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VotoResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.debug("Buscando voto por ID: {}", id);
        return ResponseEntity.ok(votoServiceV1.buscarPorId(id));
    }
}
