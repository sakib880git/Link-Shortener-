package com.wrapper.wrapper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wrapper.wrapper.dto.ApiResponse;
import com.wrapper.wrapper.dto.ChangePasswordRequest;
import com.wrapper.wrapper.dto.ForgotPasswordRequest;
import com.wrapper.wrapper.dto.RegisterRequest;
import com.wrapper.wrapper.dto.SendOtpRequest;
import com.wrapper.wrapper.dto.VerifyOtpRequest;
import com.wrapper.wrapper.service.AuthService;
import com.wrapper.wrapper.service.OtpService;
import com.wrapper.wrapper.service.RateLimitService;
import com.wrapper.wrapper.util.GetIP;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OtpService otpService;
    private final AuthService authService;

    private final RateLimitService rateLimitService;

    // private final GetIP getIp;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Object>> sendOtp(@RequestBody SendOtpRequest request,
            HttpServletRequest httpRequest) {

        String ip = GetIP.getClientIp(httpRequest);

        rateLimitService.checkIpLimit(ip);

        otpService.sendOtp(request.getEmail());

        return ResponseEntity.ok(
                new ApiResponse<>(200,
                        "OTP sent successfully",
                        null));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {

        String ip = GetIP.getClientIp(httpRequest);

        rateLimitService.checkIpLimit(ip);

        authService.register(request);

        return ResponseEntity.ok(
                new ApiResponse<>(200,
                        "User registered successfully",
                        null));
    }

    @PostMapping("/forgot-password/send-otp")
    public ResponseEntity<ApiResponse<Object>> forgotPasswordOtp(
            @RequestBody ForgotPasswordRequest request,
            HttpServletRequest httpRequest) {

        String ip = GetIP.getClientIp(httpRequest);

        rateLimitService.checkIpLimit(ip);

        authService.sendForgotPasswordOtp(request.getEmail());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "OTP sent successfully.",
                        null));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest) {

        String ip = GetIP.getClientIp(httpRequest);

        rateLimitService.checkIpLimit(ip);

        authService.changePassword(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Password changed successfully.",
                        null));
    }

}