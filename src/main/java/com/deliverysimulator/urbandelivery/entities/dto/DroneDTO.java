package com.deliverysimulator.urbandelivery.entities.dto;

import com.deliverysimulator.urbandelivery.entities.Drone;
import com.deliverysimulator.urbandelivery.entities.StatusDrone;

public class DroneDTO {
    private int id;
    private double capacidadeKg;
    private double alcanceKm;
    private StatusDrone status;

    public DroneDTO() {
    }

    public DroneDTO(int id, double capacidadeKg, double alcanceKm, StatusDrone status) {
        this.id = id;
        this.capacidadeKg = capacidadeKg;
        this.alcanceKm = alcanceKm;
        this.status = status;
    }

    public static DroneDTO of(Drone d) {
        return new DroneDTO(d.getId(), d.getCapacidadeKg(), d.getAlcanceKm(), d.getStatus());
    }

    public int getId() {
        return id;
    }

    public double getCapacidadeKg() {
        return capacidadeKg;
    }

    public double getAlcanceKm() {
        return alcanceKm;
    }

    public StatusDrone getStatus() {
        return status;
    }
}
