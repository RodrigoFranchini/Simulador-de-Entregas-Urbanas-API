package com.deliverysimulator.urbandelivery.entities.dto;

import com.deliverysimulator.urbandelivery.entities.StatusViagem;

import java.util.List;

public class FilaViagemDTO {
    private Integer viagemId;
    private Integer droneId;
    private StatusViagem status;
    private List<FilaEntregaDTO> entregas;

    public FilaViagemDTO(Integer viagemId, Integer droneId, StatusViagem status, List<FilaEntregaDTO> entregas) {
        this.viagemId = viagemId;
        this.droneId = droneId;
        this.status = status;
        this.entregas = entregas;
    }

    public Integer getViagemId() {
        return viagemId;
    }

    public Integer getDroneId() {
        return droneId;
    }

    public StatusViagem getStatus() {
        return status;
    }

    public List<FilaEntregaDTO> getEntregas() {
        return entregas;
    }
}
