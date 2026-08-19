package com.miranda.opencord.server.application.dto;

import com.miranda.opencord.channel.domain.ChannelType;

import java.util.UUID;

public record CreateServerChannelCommand(
        UUID serverId,
        String name,
        ChannelType type,
        UUID requesterId
) {
}
