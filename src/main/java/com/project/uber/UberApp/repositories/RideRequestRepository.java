package com.project.uber.UberApp.repositories;

import com.project.uber.UberApp.entities.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
}
