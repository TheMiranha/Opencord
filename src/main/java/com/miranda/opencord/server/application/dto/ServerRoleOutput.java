package com.miranda.opencord.server.application.dto;

import java.util.UUID;

public record ServerRoleOutput(
        UUID id,
        UUID serverId,
        String name,
        String color,
        Integer position,
        Long permissions
) {}
