package com.miranda.opencord.server.application.dto;

import java.time.Instant;
import java.util.UUID;

public record ServerBanOutput(
        UUID id,
        UUID userId,
        String username,
        String avatarUrl,
        String reason,
        String bannedByName,
        Instant createdAt
) {}
