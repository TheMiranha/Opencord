package com.miranda.opencord.message.application.dto;

import java.time.Instant;
import java.util.UUID;

public record SendMessageOutput(
        String id,
        UUID senderId,
        String senderUsername,
        String senderAvatarUrl,
        UUID channelId,
        String content,
        Instant createdAt
) {}