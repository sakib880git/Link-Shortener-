package com.wrapper.wrapper.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wrapper.wrapper.dto.RegisterRequest;
import com.wrapper.wrapper.entity.User;
import com.wrapper.wrapper.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {

        otpService.verifyOtp(
                request.getEmail(),
                request.getOtp());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // if (!otpService.isVerified(request.getEmail())) {
        // throw new RuntimeException("Email not verified");
        // }

        if (request.getPassword().length() > 30 || request.getPassword().length() < 8) {
            throw new RuntimeException("Password must be between 8 and 30 characters long.");
        }
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}