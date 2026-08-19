package com.wrapper.wrapper.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wrapper.wrapper.dto.CreateShortLinkRequest;
import com.wrapper.wrapper.dto.ShortLinkResponse;
import com.wrapper.wrapper.entity.ShortLink;
import com.wrapper.wrapper.entity.User;
import com.wrapper.wrapper.exception.ApiException;
import com.wrapper.wrapper.repository.ShortLinkRepository;
import com.wrapper.wrapper.repository.UserRepository;
import com.wrapper.wrapper.util.ShortCodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShortLinkService {

    private final ShortLinkRepository shortLinkRepository;
    private final UserRepository userRepository;
    private final ShortCodeGenerator shortCodeGenerator;

    @Transactional
    public ShortLinkResponse createShortLink(
            String email,
            CreateShortLinkRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ApiException("User not found."));

        String shortCode;

        do {
            shortCode = shortCodeGenerator.generate();
        } while (shortLinkRepository.existsByShortCode(shortCode));

        ShortLink shortLink = new ShortLink();

        shortLink.setOriginalUrl(request.getOriginalUrl());
        shortLink.setShortCode(shortCode);
        shortLink.setUser(user);
        shortLink.setClickCount(0L);
        shortLink.setActive(true);
        shortLink.setCreatedAt(LocalDateTime.now());

        ShortLink saved =
                shortLinkRepository.save(shortLink);

        String shortUrl =
                "http://localhost:8080/" + saved.getShortCode();

        return new ShortLinkResponse(
                saved.getId(),
                saved.getOriginalUrl(),
                saved.getShortCode(),
                shortUrl,
                saved.getClickCount(),
                saved.isActive(),
                saved.getCreatedAt(),
                saved.getExpiresAt()
        );
    }

    public List<ShortLinkResponse> getUserLinks(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ApiException("User not found."));

        return shortLinkRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteLink(String email, Long id) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ApiException("User not found."));

        ShortLink link = shortLinkRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ApiException("Short link not found."));

        link.setActive(false);

        shortLinkRepository.save(link);
    }

    private ShortLinkResponse toResponse(ShortLink link) {

        String shortUrl =
                "http://localhost:8080/" + link.getShortCode();

        return new ShortLinkResponse(
                link.getId(),
                link.getOriginalUrl(),
                link.getShortCode(),
                shortUrl,
                link.getClickCount(),
                link.isActive(),
                link.getCreatedAt(),
                link.getExpiresAt()
        );
    }
}