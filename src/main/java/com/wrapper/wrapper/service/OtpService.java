package com.wrapper.wrapper.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.wrapper.wrapper.entity.EmailOtp;
import com.wrapper.wrapper.repository.EmailOtpRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final EmailOtpRepository otpRepository;
    private final EmailService emailService;

    public void sendOtp(String email) {

        String otp = String.valueOf((int)((Math.random() * 900000) + 100000));

        EmailOtp entity = new EmailOtp();

        entity.setEmail(email);
        entity.setOtp(otp);
        entity.setVerified(false);
        entity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(entity);

        emailService.sendOtp(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {

        EmailOtp entity = otpRepository
                .findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (entity.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!entity.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        entity.setVerified(true);

        otpRepository.save(entity);

        return true;
    }

    public boolean isVerified(String email) {

        return otpRepository
                .findTopByEmailOrderByIdDesc(email)
                .map(EmailOtp::isVerified)
                .orElse(false);
    }
}
