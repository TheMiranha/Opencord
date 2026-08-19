package com.miranda.opencord.friendship.application.dto;

import java.util.UUID;

public record AcceptFriendshipCommand(
        UUID userId,
        UUID friendshipId
) {
}
