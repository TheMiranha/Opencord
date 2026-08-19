package com.miranda.opencord.server.application.dto;

import java.util.UUID;

public record ServerMemberOutput(
        UUID id,
        UUID userId,
        String username,
        String role
) {}
