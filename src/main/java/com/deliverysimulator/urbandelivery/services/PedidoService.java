package com.deliverysimulator.urbandelivery.services;

import com.deliverysimulator.urbandelivery.entities.dto.PedidoDTO;
import com.deliverysimulator.urbandelivery.entities.Pedido;

import java.util.List;

public interface PedidoService {
    Pedido criarPedido(Pedido pedido);

    List<PedidoDTO> listarPedidos();
}
