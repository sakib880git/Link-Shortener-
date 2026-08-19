package com.wrapper.wrapper.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wrapper.wrapper.dto.ApiResponse;
import com.wrapper.wrapper.dto.CreateShortLinkRequest;
import com.wrapper.wrapper.dto.ShortLinkResponse;
import com.wrapper.wrapper.service.ShortLinkService;
import com.wrapper.wrapper.util.SecurityUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShortLinkResponse>>
            createShortLink(
                    @Valid @RequestBody CreateShortLinkRequest request) {

        String email =
                SecurityUtil.getCurrentUserEmail();

        ShortLinkResponse response =
                shortLinkService.createShortLink(
                        email,
                        request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    new ApiResponse<>(
                        201,
                        "Short link created successfully",
                        response
                    )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShortLinkResponse>>>
            getMyLinks() {

        String email =
                SecurityUtil.getCurrentUserEmail();

        List<ShortLinkResponse> links =
                shortLinkService.getUserLinks(email);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Links fetched successfully",
                        links
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>>
            deleteLink(@PathVariable Long id) {

        String email =
                SecurityUtil.getCurrentUserEmail();

        shortLinkService.deleteLink(email, id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Short link deleted successfully",
                        null
                )
        );
    }
}