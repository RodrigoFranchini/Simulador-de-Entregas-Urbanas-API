package com.deliverysimulator.urbandelivery.repositories;

import com.deliverysimulator.urbandelivery.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
