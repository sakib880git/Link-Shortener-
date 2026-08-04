package com.wrapper.wrapper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wrapper.wrapper.dto.ApiResponse;
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

    private final GetIP getIp;
    // private String getClientIp(HttpServletRequest request) {

    //     String forwarded = request.getHeader("X-Forwarded-For");

    //     if (forwarded != null && !forwarded.isBlank()) {
    //         return forwarded.split(",")[0].trim();
    //     }

    //     return request.getRemoteAddr();
    // }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Object>> sendOtp(@RequestBody SendOtpRequest request,
            HttpServletRequest httpRequest) {

        String ip = getIp.getClientIp(httpRequest);

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

        String ip = getIp.getClientIp(httpRequest);

        rateLimitService.checkIpLimit(ip);

        authService.register(request);

        return ResponseEntity.ok(
                new ApiResponse<>(200,
                        "User registered successfully",
                        null));
    }
}