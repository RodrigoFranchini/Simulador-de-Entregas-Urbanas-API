package com.deliverysimulator.urbandelivery.entities.dto;

import com.deliverysimulator.urbandelivery.entities.Pedido;
import com.deliverysimulator.urbandelivery.entities.PrioridadeEntrega;
import com.deliverysimulator.urbandelivery.entities.StatusPedido;

public class PedidoDTO {
    private int id;
    private String destinoGrid;
    private double pesoKg;
    private PrioridadeEntrega prioridade;
    private StatusPedido status;

    public PedidoDTO() {
    }

    public PedidoDTO(int id, String destinoGrid, double pesoKg,
                     PrioridadeEntrega prioridade, StatusPedido status) {
        this.id = id;
        this.destinoGrid = destinoGrid;
        this.pesoKg = pesoKg;
        this.prioridade = prioridade;
        this.status = status;
    }

    public static PedidoDTO of(Pedido p) {
        return new PedidoDTO(p.getId(), p.getDestinoGrid(), p.getPesoKg(), p.getPrioridade(), p.getStatus());
    }

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
}
