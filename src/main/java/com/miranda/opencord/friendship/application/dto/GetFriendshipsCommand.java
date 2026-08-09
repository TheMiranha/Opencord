package com.miranda.opencord.friendship.application.dto;

import java.util.UUID;

public record GetFriendshipsCommand(
        UUID userId
) {
}
