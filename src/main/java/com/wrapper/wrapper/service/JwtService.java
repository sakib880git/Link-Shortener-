package com.wrapper.wrapper.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Keep this in application.properties in production
    private static final String SECRET =
            "my-super-secret-key-for-wrapper-link-shortener-project-2026";

    private final SecretKey key = Keys.hmacShaKeyFor(
            SECRET.getBytes());

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24; // 24 hours

    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis()
                                + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
}