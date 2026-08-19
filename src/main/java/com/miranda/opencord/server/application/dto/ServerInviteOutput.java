package com.miranda.opencord.server.application.dto;

import java.time.Instant;
import java.util.UUID;

public record ServerInviteOutput(
        UUID id,
        String code,
        UUID serverId,
        String inviterUsername,
        Instant createdAt
) {}
