package com.wrapper.wrapper.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.wrapper.wrapper.entity.EmailOtp;
import com.wrapper.wrapper.exception.ApiException;
import com.wrapper.wrapper.repository.EmailOtpRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final EmailOtpRepository otpRepository;
    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;


    public boolean isVerified(String email) {

        return otpRepository
                .findTopByEmailOrderByIdDesc(email)
                .map(EmailOtp::isVerified)
                .orElse(false);
    }

    public void sendOtp(String email) {

        String key = "OTP:" + email;

        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        if (ttl != null && ttl > 0) {

            long minutes = ttl / 60;
            long seconds = ttl % 60;

            throw new ApiException(
                    String.format(
                            "OTP already sent. Please try again in %d minute(s) %d second(s).",
                            minutes,
                            seconds));
        }

        String otp = String.format("%06d",
                ThreadLocalRandom.current().nextInt(100000, 1000000));

        redisTemplate.opsForValue()
                .set(key, otp, Duration.ofMinutes(5));

        System.out.println("OTP : " + otp);
        // emailService.sendOtp(email, otp);
    }

    public void verifyOtp(String email, String otp) {

        String storedOtp = redisTemplate.opsForValue()
                .get("OTP:" + email);
        
        System.out.println("extractedOTP : " +  storedOtp);

        if (storedOtp == null) {
            throw new ApiException("OTP expired");
        }

        if (!storedOtp.equals(otp)) {
            throw new ApiException("Invalid OTP");
        }

        redisTemplate.delete("OTP:" + email);
    }
}

