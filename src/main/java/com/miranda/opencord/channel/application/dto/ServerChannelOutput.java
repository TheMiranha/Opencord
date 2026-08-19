package com.miranda.opencord.channel.application.dto;

import com.miranda.opencord.channel.domain.ChannelType;
import java.util.UUID;

public record ServerChannelOutput(
        UUID id,
        String name,
        ChannelType type,
        UUID serverId
) {}