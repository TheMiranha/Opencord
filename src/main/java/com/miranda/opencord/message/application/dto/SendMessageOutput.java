package com.miranda.opencord.message.application.dto;

import java.util.UUID;

public record SendMessageOutput(
        UUID senderId,
        UUID channelId,
        String content
) {
}
