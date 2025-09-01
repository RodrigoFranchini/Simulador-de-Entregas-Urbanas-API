package com.deliverysimulator.urbandelivery.repositories;

import com.deliverysimulator.urbandelivery.entities.Drone;
import com.deliverysimulator.urbandelivery.entities.StatusDrone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Integer> {
    List<Drone> findByStatus(StatusDrone status);
}
