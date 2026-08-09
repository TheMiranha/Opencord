package com.miranda.opencord.channel.application.dto;

import java.util.UUID;

public record ChannelMemberOutput(
        UUID id,
        String username
) {
}
