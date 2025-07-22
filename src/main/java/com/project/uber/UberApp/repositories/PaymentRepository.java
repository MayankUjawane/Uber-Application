package com.project.uber.UberApp.repositories;

import com.project.uber.UberApp.entities.Payment;
import com.project.uber.UberApp.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRide(Ride ride);
}
