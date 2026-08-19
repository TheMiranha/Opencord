package com.miranda.opencord.user.infrastructure.controller.dto;

import java.time.Instant;
import java.util.UUID;

public record MeResponse(
        UUID id,
        String username,
        String email,
        String avatarUrl,
        String bio,
        String customStatus,
        Instant createdAt,
        Instant updatedAt
) {
}
