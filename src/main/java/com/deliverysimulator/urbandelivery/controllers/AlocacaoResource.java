package com.deliverysimulator.urbandelivery.controllers;

import com.deliverysimulator.urbandelivery.services.AlocacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/alocacoes")
public class AlocacaoResource {

    private final AlocacaoService alocacaoService;

    public AlocacaoResource(AlocacaoService alocacaoService) {
        this.alocacaoService = alocacaoService;
    }

    /**
     * Dispara o processo de alocação dos pedidos pendentes em viagens.
     *
     * @return {"viagensCriadas": <qtd>}
     * POST /alocacoes/run
     */
    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> run() {
        int viagens = alocacaoService.alocarPedidos();
        return ResponseEntity.ok(Map.of("viagensCriadas", viagens));
    }
}
