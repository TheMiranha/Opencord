package com.miranda.opencord.channel.application.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelOutput(
    UUID id,
    List<ChannelMemberOutput> members,
    Instant createdAt
) {
}
