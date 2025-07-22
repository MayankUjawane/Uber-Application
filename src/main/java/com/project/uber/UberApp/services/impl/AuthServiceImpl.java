package com.project.uber.UberApp.services.impl;

import com.project.uber.UberApp.dto.DriverDto;
import com.project.uber.UberApp.dto.SignupDto;
import com.project.uber.UberApp.dto.UserDto;
import com.project.uber.UberApp.entities.User;
import com.project.uber.UberApp.enums.Role;
import com.project.uber.UberApp.exceptions.RuntimeConflictException;
import com.project.uber.UberApp.repositories.UserRepository;
import com.project.uber.UberApp.services.AuthService;
import com.project.uber.UberApp.services.RiderService;
import com.project.uber.UberApp.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;

    @Override
    public String login(String email, String password) {
        return null;
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {
        if(userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new RuntimeConflictException("Cannot signup, user already exists with email " + signupDto.getEmail());
        }

        User user = modelMapper.map(signupDto, User.class);
        user.setRoles(Set.of(Role.RIDER));
        User savedUser = userRepository.save(user);

        // create user related entities
        riderService.createNewRider(savedUser);
        walletService.createNewWallet(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId) {
        return null;
    }
}
