package com.miranda.opencord.user.infrastructure.controller.dto;

import java.time.Instant;

public record MeResponse(
        String username,
        String email,
        Instant createdAt,
        Instant updatedAt
) {
}
