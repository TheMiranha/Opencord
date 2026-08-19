package com.miranda.opencord.user.application.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserProfileOutput(
        UUID id,
        String username,
        String email,
        String avatarUrl,
        String bio,
        String customStatus,
        Instant createdAt,
        Instant updatedAt,
        int mutualFriendsCount,
        List<MutualServerOutput> mutualServers,
        int mutualServersCount
) {
}
