package com.deliverysimulator.urbandelivery.repositories;

import com.deliverysimulator.urbandelivery.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
