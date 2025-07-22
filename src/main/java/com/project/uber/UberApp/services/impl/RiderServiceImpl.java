package com.project.uber.UberApp.services.impl;

import com.project.uber.UberApp.dto.DriverDto;
import com.project.uber.UberApp.dto.RideDto;
import com.project.uber.UberApp.dto.RideRequestDto;
import com.project.uber.UberApp.dto.RiderDto;
import com.project.uber.UberApp.entities.*;
import com.project.uber.UberApp.enums.RideRequestStatus;
import com.project.uber.UberApp.enums.RideStatus;
import com.project.uber.UberApp.exceptions.ResourceNotFoundException;
import com.project.uber.UberApp.repositories.RideRequestRepository;
import com.project.uber.UberApp.repositories.RiderRepository;
import com.project.uber.UberApp.services.DriverService;
import com.project.uber.UberApp.services.RideService;
import com.project.uber.UberApp.services.RiderService;
import com.project.uber.UberApp.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideService rideService;
    private final DriverService driverService;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        List<Driver> drivers = rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);

        return modelMapper.map(savedRideRequest, RideRequestDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);
        if(!ride.getRider().equals(rider)) {
            throw new RuntimeException("Rider can cancel rides which belongs to them");
        }
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride can not be cancelled, current status of ride is : " + ride.getRideStatus());
        }

        driverService.updateDriverAvailability(ride.getDriver(), true);

        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public RiderDto getMyProfile() {
        Rider rider = getCurrentRider();
        return modelMapper.map(rider, RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Rider rider = getCurrentRider();
        return rideService.getAllRidesOfRider(rider, pageRequest).map(
                ride -> modelMapper.map(ride, RideDto.class)
        );
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0d)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        return riderRepository.findById(1l)
                .orElseThrow(() -> new ResourceNotFoundException("Rider not found"));
    }
}
