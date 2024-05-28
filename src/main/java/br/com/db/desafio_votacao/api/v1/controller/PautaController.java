package br.com.db.desafio_votacao.api.v1.controller;

import br.com.db.desafio_votacao.api.v1.dto.PautaDTO;
import br.com.db.desafio_votacao.api.v1.dto.mapper.PautaMapper;
import br.com.db.desafio_votacao.api.v1.dto.response.PautaResponseDTO;
import br.com.db.desafio_votacao.model.Pauta;
import br.com.db.desafio_votacao.service.PautaServiceV1Impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

    private static final Logger logger = LoggerFactory.getLogger(PautaController.class);

    @Autowired
    private PautaServiceV1Impl pautaServiceV1;

    @Autowired
    private PautaMapper pautaMapper;

    @PostMapping
    public ResponseEntity<PautaResponseDTO> criarPauta(@RequestBody PautaDTO pautaDTO) {
        logger.debug("Criando uma nova pauta: {}", pautaDTO);
        return ResponseEntity.ok(pautaServiceV1.salvar(pautaDTO));
    }

    @GetMapping
    public ResponseEntity<Page<PautaResponseDTO>> listarTodos(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        logger.debug("Listando todas as pautas");
        Page<PautaResponseDTO> pautas = pautaServiceV1.listarTodos(PageRequest.of(page,size));
        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PautaResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.debug("Buscando pauta por ID: {}", id);
        PautaResponseDTO pauta = pautaServiceV1.buscarPorId(id);
        if (pauta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pauta);
    }
}