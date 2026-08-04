package com.wrapper.wrapper.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String email;

    private String otp;

    private String newPassword;

    private String confirmPassword;
}