package com.wrapper.wrapper.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wrapper.wrapper.dto.ChangePasswordRequest;
import com.wrapper.wrapper.dto.RegisterRequest;
import com.wrapper.wrapper.dto.Login;
import com.wrapper.wrapper.entity.User;
import com.wrapper.wrapper.exception.ApiException;
import com.wrapper.wrapper.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public String register(RegisterRequest request) {

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

        return jwtService.generateToken(user.getEmail());
    }

    public void sendForgotPasswordOtp(String email) {

        if (!userRepository.existsByEmail(email)) {
            throw new ApiException("Email not registered.");
        }

        otpService.sendOtp(email);
    }

    public void changePassword(ChangePasswordRequest request) {

        otpService.verifyOtp(
                request.getEmail(),
                request.getOtp());

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ApiException("Passwords do not match.");
        }

        if (request.getNewPassword().length() < 8 ||
                request.getNewPassword().length() > 30) {

            throw new ApiException(
                    "Password must be between 8 and 30 characters.");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("User not found."));

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    public String login(Login login) {
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new ApiException("Invalid email or password."));

        boolean isMatch = passwordEncoder.matches(
                login.getPassword(),
                user.getPassword());

        if( !isMatch){
             throw new ApiException("Invalid email or password.");
        }

        return jwtService.generateToken(user.getEmail());
    }
}