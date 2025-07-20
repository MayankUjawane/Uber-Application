package com.project.uber.UberApp.strategies.impl;

import com.project.uber.UberApp.entities.RideRequest;
import com.project.uber.UberApp.services.DistanceService;
import com.project.uber.UberApp.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareDefaultFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(rideRequest.getPickupLocation(), rideRequest.getDropOffLocation());
        double fare = distance * RIDE_FARE_MULTIPLIER;
        return fare;
    }
}
