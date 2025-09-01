package com.deliverysimulator.urbandelivery.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Destino em malha alfanumérica (ex.: "2D"), limitado a [1-4][A-D]
    @Column(name = "destino_grid", nullable = false, length = 2)
    private String destinoGrid;

    @Column(name = "peso_kg", nullable = false)
    private double pesoKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeEntrega prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.PENDENTE;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Pedido() {
    }

    public Pedido(String destinoGrid, double pesoKg, PrioridadeEntrega prioridade) {
        this.destinoGrid = destinoGrid;
        this.pesoKg = pesoKg;
        this.prioridade = prioridade;
    }

    @PrePersist
    public void prePersist() {
        this.criadoEm = Instant.now();
        if (this.status == null) this.status = StatusPedido.PENDENTE;
    }

    // Getters/Setters
    public int getId() {
        return id;
    }

    public String getDestinoGrid() {
        return destinoGrid;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public PrioridadeEntrega getPrioridade() {
        return prioridade;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDestinoGrid(String destinoGrid) {
        this.destinoGrid = destinoGrid;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public void setPrioridade(PrioridadeEntrega prioridade) {
        this.prioridade = prioridade;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
