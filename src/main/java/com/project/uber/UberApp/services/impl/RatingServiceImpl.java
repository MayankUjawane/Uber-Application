package com.project.uber.UberApp.services.impl;

import com.project.uber.UberApp.dto.DriverDto;
import com.project.uber.UberApp.dto.RiderDto;
import com.project.uber.UberApp.entities.Driver;
import com.project.uber.UberApp.entities.Rating;
import com.project.uber.UberApp.entities.Ride;
import com.project.uber.UberApp.entities.Rider;
import com.project.uber.UberApp.exceptions.ResourceNotFoundException;
import com.project.uber.UberApp.exceptions.RuntimeConflictException;
import com.project.uber.UberApp.repositories.DriverRepository;
import com.project.uber.UberApp.repositories.RatingRepository;
import com.project.uber.UberApp.repositories.RiderRepository;
import com.project.uber.UberApp.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: " + ride.getId()));

        if(ratingObj.getDriverRating() != null) {
            throw new RuntimeConflictException("Driver has already been rated");
        }

        ratingObj.setDriverRating(rating);
        ratingRepository.save(ratingObj);

        Driver driver = ride.getDriver();
        List<Rating> driverRatings = ratingRepository.findByDriver(driver);
        Double driverUpdatedRating = driverRatings
                .stream()
                .mapToDouble(prevRating -> prevRating.getDriverRating())
                .average()
                .orElse(0.0);

        driver.setRating(driverUpdatedRating);
        Driver savedDriver = driverRepository.save(driver);

        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: " + ride.getId()));

        if(ratingObj.getRiderRating() != null) {
            throw new RuntimeConflictException("Rider has already been rated");
        }

        ratingObj.setRiderRating(rating);
        ratingRepository.save(ratingObj);

        Rider rider = ride.getRider();
        List<Rating> ridersRating = ratingRepository.findByRider(rider);
        Double riderUpdatedRating = ridersRating.stream()
                .mapToDouble(prevRating -> prevRating.getRiderRating())
                .average()
                .orElse(0.0);

        rider.setRating(riderUpdatedRating);
        riderRepository.save(rider);

        return modelMapper.map(rider, RiderDto.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();
        ratingRepository.save(rating);
    }
}
