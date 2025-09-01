package com.deliverysimulator.urbandelivery.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "entrega")
public class Entrega implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relacionamento muitos para um
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "viagem_id")
    private Viagem viagem;

    // Relacionamento muitos para um
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEntrega status = StatusEntrega.PENDENTE;

    public Entrega() {
    }

    public Entrega(Viagem viagem, Pedido pedido) {
        this.viagem = viagem;
        this.pedido = pedido;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public Viagem getViagem() {
        return viagem;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public StatusEntrega getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
    }
}
