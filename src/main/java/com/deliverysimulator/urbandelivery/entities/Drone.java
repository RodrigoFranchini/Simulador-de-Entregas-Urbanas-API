package com.deliverysimulator.urbandelivery.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

@Entity
@Table(name = "drone")
public class Drone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Positive
    @Column(name = "capacidade_kg", nullable = false)
    private double capacidadeKg;

    @Positive
    @Column(name = "alcance_km", nullable = false)
    private double alcanceKm;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusDrone status = StatusDrone.DISPONIVEL;

    public Drone() {
    }

    public Drone(double capacidadeKg, double alcanceKm, StatusDrone status) {
        this.capacidadeKg = capacidadeKg;
        this.alcanceKm = alcanceKm;
        this.status = status;
    }

    // Getters e Setters
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

    public void setId(int id) {
        this.id = id;
    }

    public void setCapacidadeKg(double capacidadeKg) {
        this.capacidadeKg = capacidadeKg;
    }

    public void setAlcanceKm(double alcanceKm) {
        this.alcanceKm = alcanceKm;
    }

    public void setStatus(StatusDrone status) {
        this.status = status;
    }
}
