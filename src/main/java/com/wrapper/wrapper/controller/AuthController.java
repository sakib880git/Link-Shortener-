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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OtpService otpService;
    private final AuthService authService;

    

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Object>> sendOtp(@RequestBody SendOtpRequest request) {

        otpService.sendOtp(request.getEmail());

        return ResponseEntity.ok(
                new ApiResponse<>(200,
                        "OTP sent successfully",
                        null));
    }

//     @PostMapping("/verify-otp")
//     public ResponseEntity<ApiResponse<Object>> verifyOtp(@RequestBody VerifyOtpRequest request) {

//         otpService.verifyOtp(request.getEmail(), request.getOtp());

//         return ResponseEntity.ok(
//         new ApiResponse<>(200,
//                 "OTP verified successfully",
//                 null)
// );
//     }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok(
        new ApiResponse<>(200,
                "User registered successfully",
                null)
);
    }
}