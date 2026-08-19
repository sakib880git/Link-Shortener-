package com.wrapper.wrapper.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.wrapper.wrapper.exception.ApiException;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static String getCurrentUserEmail() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new ApiException("User is not authenticated.");
        }

        return authentication.getName();
    }
}