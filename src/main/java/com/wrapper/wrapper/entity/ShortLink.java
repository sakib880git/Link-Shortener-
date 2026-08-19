package com.wrapper.wrapper.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "short_links",
    indexes = {
        @Index(name = "idx_short_code", columnList = "short_code"),
        @Index(name = "idx_user_id", columnList = "user_id")
    }
)
@Getter
@Setter
public class ShortLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_url", nullable = false, length = 2048)
    private String originalUrl;

    @Column(
        name = "short_code",
        nullable = false,
        unique = true,
        length = 20
    )
    private String shortCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "click_count", nullable = false)
    private Long clickCount = 0L;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}