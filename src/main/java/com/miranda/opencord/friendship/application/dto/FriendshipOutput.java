package com.miranda.opencord.friendship.application.dto;

import java.util.UUID;

public record FriendshipOutput(
        UUID id,
        String username,
        String avatarUrl,
        UUID userId
) {
}
