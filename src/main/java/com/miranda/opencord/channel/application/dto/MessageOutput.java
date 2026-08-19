package com.miranda.opencord.channel.application.dto;

import com.miranda.opencord.message.application.dto.MessageAttachmentDto;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record MessageOutput(
        String id,
        UUID channelId,
        UUID senderId,
        String senderUsername,
        String senderAvatarUrl,
        String content,
        List<MessageAttachmentDto> attachments,
        Instant createdAt
) {}
