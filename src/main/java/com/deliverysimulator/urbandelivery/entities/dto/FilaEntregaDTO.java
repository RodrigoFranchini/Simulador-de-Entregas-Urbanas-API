package com.deliverysimulator.urbandelivery.entities.dto;

import com.deliverysimulator.urbandelivery.entities.PrioridadeEntrega;
import com.deliverysimulator.urbandelivery.entities.StatusPedido;

public class FilaEntregaDTO {
    private int pedidoId;
    private String destinoGrid;
    private double distanciaDaBase;
    private double pesoKg;
    private PrioridadeEntrega prioridade;
    private StatusPedido statusPedido;
    private Integer ordemNaViagem;

    public FilaEntregaDTO(int pedidoId, String destinoGrid, double distanciaDaBase,
                          double pesoKg, PrioridadeEntrega prioridade, StatusPedido statusPedido,
                          Integer ordemNaViagem) {
        this.pedidoId = pedidoId;
        this.destinoGrid = destinoGrid;
        this.distanciaDaBase = distanciaDaBase;
        this.pesoKg = pesoKg;
        this.prioridade = prioridade;
        this.statusPedido = statusPedido;
        this.ordemNaViagem = ordemNaViagem;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public String getDestinoGrid() {
        return destinoGrid;
    }

    public double getDistanciaDaBase() {
        return distanciaDaBase;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public PrioridadeEntrega getPrioridade() {
        return prioridade;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public Integer getOrdemNaViagem() {
        return ordemNaViagem;
    }
}
