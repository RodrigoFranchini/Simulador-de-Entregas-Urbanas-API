package com.deliverysimulator.urbandelivery.controllers;

import com.deliverysimulator.urbandelivery.entities.dto.ViagemDTO;
import com.deliverysimulator.urbandelivery.services.ViagemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/viagens")
public class ViagemResource {

    private final ViagemService viagemService;

    public ViagemResource(ViagemService viagemService) {
        this.viagemService = viagemService;
    }

    // Finaliza a viagem: pedidos e entregas -> ENTREGUE, viagem -> CONCLUIDA, drone -> DISPONIVEL
    // POST /viagens/{id}/finalizar
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<ViagemDTO> finalizar(@PathVariable int id) {
        ViagemDTO dto = viagemService.finalizarViagem(id);
        return ResponseEntity.ok(dto);
    }


}
