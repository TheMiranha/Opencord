package com.miranda.opencord.message.infrastructure.controller.dto;

import java.util.UUID;

public record ChatMessageOutput(
        UUID senderId,
        UUID channelId,
        String content
) {
}
