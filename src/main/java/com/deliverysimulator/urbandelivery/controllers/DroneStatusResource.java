package com.deliverysimulator.urbandelivery.controllers;

import com.deliverysimulator.urbandelivery.entities.dto.DroneDTO;
import com.deliverysimulator.urbandelivery.entities.Drone;
import com.deliverysimulator.urbandelivery.entities.StatusDrone;
import com.deliverysimulator.urbandelivery.repositories.DroneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/drones")
public class DroneStatusResource {

    private final DroneRepository droneRepository;

    public DroneStatusResource(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    // GET /drones/status -> todos
    // GET /drones/status?status=DISPONIVEL -> filtrado
    @GetMapping("/status")
    public ResponseEntity<List<DroneDTO>> listar(@RequestParam(required = false) String status) {
        List<Drone> drones;
        if (status == null || status.isBlank()) {
            drones = droneRepository.findAll();
        } else {
            StatusDrone st = StatusDrone.valueOf(status.toUpperCase(Locale.ROOT));
            drones = droneRepository.findByStatus(st);
        }
        var body = drones.stream().map(DroneDTO::of).collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    // GET /drones/{id}/status
    @GetMapping("/{id}/status")
    public ResponseEntity<DroneDTO> obter(@PathVariable int id) {
        Drone d = droneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drone não encontrado: " + id));
        return ResponseEntity.ok(DroneDTO.of(d));
    }
}
