package com.miranda.opencord.message.infrastructure.controller.dto;

import java.util.UUID;

public record ChatMessageRequest(
        UUID channelId,
        String content
) {
}
