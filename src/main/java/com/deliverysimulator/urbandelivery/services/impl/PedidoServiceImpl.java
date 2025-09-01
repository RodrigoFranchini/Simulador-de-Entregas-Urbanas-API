package com.deliverysimulator.urbandelivery.services.impl;

import com.deliverysimulator.urbandelivery.entities.*;
import com.deliverysimulator.urbandelivery.repositories.EntregaRepository;
import com.deliverysimulator.urbandelivery.repositories.PedidoRepository;
import com.deliverysimulator.urbandelivery.repositories.ViagemRepository;
import com.deliverysimulator.urbandelivery.services.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.deliverysimulator.urbandelivery.entities.dto.PedidoDTO;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ViagemRepository viagemRepository;
    private final EntregaRepository entregaRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             ViagemRepository viagemRepository,
                             EntregaRepository entregaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.viagemRepository = viagemRepository;
        this.entregaRepository = entregaRepository;
    }

    @Override
    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        // Pedido nasce PENDENTE
        pedido.setStatus(StatusPedido.PENDENTE);
        Pedido salvo = pedidoRepository.save(pedido);

        // Cria Viagem sem drone (será associada depois, na alocação)
        Viagem viagem = new Viagem();
        viagem.setDrone(null);
        viagem.setStatus(StatusViagem.PLANEJADA);
        viagem.setPesoTotalKg(pedido.getPesoKg());
        viagem.setDistanciaTotalKm(0.0);
        viagem = viagemRepository.save(viagem);

        // Cria Entrega ligando Pedido ↔ Viagem
        Entrega entrega = new Entrega();
        entrega.setViagem(viagem);
        entrega.setPedido(salvo);
        entrega.setStatus(StatusEntrega.PENDENTE);
        entregaRepository.save(entrega);

        return salvo;
    }

    @Override
    public List<PedidoDTO> listarPedidos() {
        var todos = pedidoRepository.findAll(); // pode vir com repetidos em alguns cenários
        Map<Integer, com.deliverysimulator.urbandelivery.entities.Pedido> porId = new LinkedHashMap<>();
        for (var p : todos) porId.putIfAbsent(p.getId(), p); // dedup por id preservando ordem
        return porId.values().stream().map(PedidoDTO::of).toList();
    }
}
