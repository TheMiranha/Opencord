package com.miranda.opencord.channel.application.dto;

import java.util.UUID;

public record ChannelWebSocketEvent(
        String event,
        UUID serverId,
        UUID channelId,
        ServerChannelOutput channel
) {
}
