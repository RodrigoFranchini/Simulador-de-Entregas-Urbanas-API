package com.deliverysimulator.urbandelivery.services;

import com.deliverysimulator.urbandelivery.entities.dto.ViagemDTO;

public interface ViagemService {
    ViagemDTO finalizarViagem(int viagemId);
}
