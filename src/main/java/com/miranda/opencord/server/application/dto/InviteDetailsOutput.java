package com.miranda.opencord.server.application.dto;

import java.util.UUID;

public record InviteDetailsOutput(
        String code,
        UUID serverId,
        String serverName,
        String inviterUsername
) {}