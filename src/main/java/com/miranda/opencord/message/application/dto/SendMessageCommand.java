package com.miranda.opencord.message.application.dto;

import java.util.List;
import java.util.UUID;

public record SendMessageCommand(
        UUID senderId,
        UUID channelId,
        String content,
        List<MessageAttachmentDto> attachments
) {}
