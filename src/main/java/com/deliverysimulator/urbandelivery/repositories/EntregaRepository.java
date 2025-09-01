package com.deliverysimulator.urbandelivery.repositories;

import com.deliverysimulator.urbandelivery.entities.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
    List<Entrega> findByViagem_Id(int viagemId);
}
