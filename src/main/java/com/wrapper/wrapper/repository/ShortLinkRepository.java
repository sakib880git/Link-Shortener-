package com.wrapper.wrapper.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wrapper.wrapper.entity.ShortLink;
import com.wrapper.wrapper.entity.User;

public interface ShortLinkRepository
        extends JpaRepository<ShortLink, Long> {

    Optional<ShortLink> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    List<ShortLink> findByUserOrderByCreatedAtDesc(User user);

    Optional<ShortLink> findByIdAndUser(Long id, User user);
}