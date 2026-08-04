package com.wrapper.wrapper.util;

import jakarta.servlet.http.HttpServletRequest;

public final class GetIP {

    private GetIP() {
    }

    public static String getClientIp(HttpServletRequest request) {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}