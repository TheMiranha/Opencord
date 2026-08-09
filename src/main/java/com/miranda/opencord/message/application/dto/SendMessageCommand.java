package com.miranda.opencord.message.application.dto;

import java.util.UUID;

public record SendMessageCommand(
        UUID senderId,
        UUID channelId,
        String content
) {
}
