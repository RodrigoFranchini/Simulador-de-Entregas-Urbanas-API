package com.deliverysimulator.urbandelivery.controllers;

import com.deliverysimulator.urbandelivery.entities.dto.FilaViagemDTO;
import com.deliverysimulator.urbandelivery.services.FilaPorViagemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fila")
public class FilaPorViagemResource {

    private final FilaPorViagemService service;

    public FilaPorViagemResource(FilaPorViagemService service) {
        this.service = service;
    }

    // GET /fila/viagens
    @GetMapping("/viagens")
    public ResponseEntity<List<FilaViagemDTO>> getFilaPorViagem() {
        return ResponseEntity.ok(service.gerarFilaPorViagem());
    }
}
