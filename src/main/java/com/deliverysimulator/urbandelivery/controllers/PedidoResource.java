package com.deliverysimulator.urbandelivery.controllers;

import com.deliverysimulator.urbandelivery.entities.Pedido;
import com.deliverysimulator.urbandelivery.entities.dto.PedidoDTO;
import com.deliverysimulator.urbandelivery.services.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoResource {

    private final PedidoService pedidoService;

    public PedidoResource(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // POST / -> cria pedidos
    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.criarPedido(pedido));
    }

    // GET /pedidos -> lista todos pedidos
    @GetMapping
    public ResponseEntity<java.util.List<PedidoDTO>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }
}
