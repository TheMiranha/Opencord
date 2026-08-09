package com.miranda.opencord.friendship.infrastructure.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public record SendFriendshipRequestRequest(
        @NotEmpty
        String addresseeUsername
) {
}
