package com.project.uber.UberApp.repositories;

import com.project.uber.UberApp.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
}
