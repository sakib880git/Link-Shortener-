package com.wrapper.wrapper.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortLinkResponse {

    private Long id;

    private String originalUrl;

    private String shortCode;

    private String shortUrl;

    private Long clickCount;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}