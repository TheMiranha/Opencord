package com.miranda.opencord.message.application.dto;

import java.time.Instant;
import java.util.UUID;

public record SendMessageOutput(
        String id,            // <- ADICIONE O ID
        UUID senderId,
        UUID channelId,
        String content,
        Instant createdAt     // <- ADICIONE A DATA
) {}