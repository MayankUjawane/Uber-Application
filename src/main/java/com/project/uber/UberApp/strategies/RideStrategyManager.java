package com.project.uber.UberApp.strategies;

import com.project.uber.UberApp.strategies.impl.DriverMatchingHighestRatedDriverStrategy;
import com.project.uber.UberApp.strategies.impl.DriverMatchingNearestDriverStrategy;
import com.project.uber.UberApp.strategies.impl.RideFareDefaultFareCalculationStrategy;
import com.project.uber.UberApp.strategies.impl.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double riderRating) {
        if(riderRating >= 4.8) {
            return highestRatedDriverStrategy;
        }
        return nearestDriverStrategy;
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy() {
        // 6 pm to 9 pm
        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();

        if(currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime)) {
            return surgePricingFareCalculationStrategy;
        }
        return defaultFareCalculationStrategy;
    }
}
