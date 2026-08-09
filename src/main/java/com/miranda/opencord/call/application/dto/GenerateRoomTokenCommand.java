package com.miranda.opencord.call.application.dto;

import java.util.UUID;

public record GenerateRoomTokenCommand(
        UUID userId,
        UUID channelId
) {
}
