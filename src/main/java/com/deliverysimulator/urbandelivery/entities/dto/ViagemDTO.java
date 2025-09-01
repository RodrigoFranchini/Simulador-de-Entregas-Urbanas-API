package com.deliverysimulator.urbandelivery.entities.dto;

import com.deliverysimulator.urbandelivery.entities.StatusViagem;

public class ViagemDTO {
    private int viagemId;
    private Integer droneId;
    private StatusViagem statusViagem;
    private int pedidosEntregues;

    public ViagemDTO(int viagemId, Integer droneId, StatusViagem statusViagem, int pedidosEntregues) {
        this.viagemId = viagemId;
        this.droneId = droneId;
        this.statusViagem = statusViagem;
        this.pedidosEntregues = pedidosEntregues;
    }

    public int getViagemId() {
        return viagemId;
    }

    public Integer getDroneId() {
        return droneId;
    }

    public StatusViagem getStatusViagem() {
        return statusViagem;
    }

    public int getPedidosEntregues() {
        return pedidosEntregues;
    }
}
