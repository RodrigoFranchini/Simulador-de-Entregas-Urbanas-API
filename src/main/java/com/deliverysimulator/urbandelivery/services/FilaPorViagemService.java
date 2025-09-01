package com.deliverysimulator.urbandelivery.services;

import com.deliverysimulator.urbandelivery.entities.dto.FilaViagemDTO;

import java.util.List;

public interface FilaPorViagemService {
    List<FilaViagemDTO> gerarFilaPorViagem();
}
