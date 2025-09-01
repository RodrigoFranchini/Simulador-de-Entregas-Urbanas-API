package com.deliverysimulator.urbandelivery.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "viagem")
public class Viagem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relacionamento muitos para um
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_id", nullable = true)
    private Drone drone;

    @Column(name = "distancia_total_km", nullable = false)
    private double distanciaTotalKm;

    @Column(name = "peso_total_kg", nullable = false)
    private double pesoTotalKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusViagem status = StatusViagem.PLANEJADA;

    // Relacionamento um para muitos
    @OneToMany(mappedBy = "viagem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entrega> entregas = new ArrayList<>();

    @Column(name = "criada_em", nullable = false, updatable = false)
    private Instant criadaEm;

    public Viagem() {
    }

    public Viagem(Drone drone, double distanciaTotalKm, double pesoTotalKg) {
        this.drone = drone;
        this.distanciaTotalKm = distanciaTotalKm;
        this.pesoTotalKg = pesoTotalKg;
    }

    @PrePersist
    public void prePersist() {
        this.criadaEm = Instant.now();
    }

    // getters/setters
    public int getId() {
        return id;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public double getDistanciaTotalKm() {
        return distanciaTotalKm;
    }

    public void setDistanciaTotalKm(double d) {
        this.distanciaTotalKm = d;
    }

    public double getPesoTotalKg() {
        return pesoTotalKg;
    }

    public void setPesoTotalKg(double p) {
        this.pesoTotalKg = p;
    }

    public StatusViagem getStatus() {
        return status;
    }

    public void setStatus(StatusViagem status) {
        this.status = status;
    }

    public List<Entrega> getEntregas() {
        return entregas;
    }
}
