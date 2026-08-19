package com.miranda.opencord.server.application.dto;

import java.util.UUID;

public record GenerateInviteOutput(
        String code,
        UUID serverId
) {}