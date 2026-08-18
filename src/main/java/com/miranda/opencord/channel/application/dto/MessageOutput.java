package com.miranda.opencord.channel.application.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record MessageOutput(
        String id,
        UUID channelId,
        UUID senderId,
        String content,
        Instant createdAt
) {
}
