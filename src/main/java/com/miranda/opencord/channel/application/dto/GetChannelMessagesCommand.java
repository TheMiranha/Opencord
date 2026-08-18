package com.miranda.opencord.channel.application.dto;

import java.util.UUID;

public record GetChannelMessagesCommand(
        UUID userId,
        UUID channelId,
        int page,
        int size
) {
}
