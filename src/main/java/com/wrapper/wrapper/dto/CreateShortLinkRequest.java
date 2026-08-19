package com.wrapper.wrapper.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateShortLinkRequest {

    @NotBlank(message = "URL is required")
    @Size(max = 2048, message = "URL cannot exceed 2048 characters")
    private String originalUrl;
}