package com.deliverysimulator.urbandelivery.services.impl;

import com.deliverysimulator.urbandelivery.entities.dto.ViagemDTO;
import com.deliverysimulator.urbandelivery.entities.*;
import com.deliverysimulator.urbandelivery.repositories.*;
import com.deliverysimulator.urbandelivery.services.ViagemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViagemServiceImpl implements ViagemService {

    private final ViagemRepository viagemRepository;
    private final EntregaRepository entregaRepository;
    private final PedidoRepository pedidoRepository;
    private final DroneRepository droneRepository;

    public ViagemServiceImpl(ViagemRepository viagemRepository,
                             EntregaRepository entregaRepository,
                             PedidoRepository pedidoRepository,
                             DroneRepository droneRepository) {
        this.viagemRepository = viagemRepository;
        this.entregaRepository = entregaRepository;
        this.pedidoRepository = pedidoRepository;
        this.droneRepository = droneRepository;
    }

    @Override
    @Transactional
    public ViagemDTO finalizarViagem(int viagemId) {
        Viagem viagem = viagemRepository.findById(viagemId)
                .orElseThrow(() -> new IllegalArgumentException("Viagem não encontrada: " + viagemId));

        if (viagem.getStatus() == StatusViagem.CONCLUIDA) {
            // já finalizada; idempotente: apenas retorna o resumo
            Integer droneId = viagem.getDrone() != null ? viagem.getDrone().getId() : null;
            int qtdEntregas = entregaRepository.findByViagem_Id(viagemId).size();
            return new ViagemDTO(viagem.getId(), droneId, viagem.getStatus(), qtdEntregas);
        }

        // 1) Entregas e Pedidos -> ENTREGUE
        List<Entrega> entregas = entregaRepository.findByViagem_Id(viagemId);
        int entregues = 0;
        for (Entrega e : entregas) {
            e.setStatus(StatusEntrega.ENTREGUE);
            entregaRepository.save(e);

            Pedido p = e.getPedido();
            if (p != null) {
                p.setStatus(StatusPedido.ENTREGUE);
                pedidoRepository.save(p);
                entregues++;
            }
        }

        // 2) Viagem -> CONCLUIDA
        viagem.setStatus(StatusViagem.CONCLUIDA);
        viagemRepository.save(viagem);

        // 3) Drone -> DISPONIVEL (se houver)
        if (viagem.getDrone() != null) {
            Drone d = viagem.getDrone();
            d.setStatus(StatusDrone.DISPONIVEL);
            droneRepository.save(d);
        }

        Integer droneId = viagem.getDrone() != null ? viagem.getDrone().getId() : null;
        return new ViagemDTO(viagem.getId(), droneId, viagem.getStatus(), entregues);
    }
}
