package com.project.uber.UberApp.repositories;

import com.project.uber.UberApp.entities.Driver;
import com.project.uber.UberApp.entities.Ride;
import com.project.uber.UberApp.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {
    Page<Ride> findByRider(Rider rider, Pageable pageRequest);
    Page<Ride> findByDriver(Driver driver, PageRequest pageRequest);

}
