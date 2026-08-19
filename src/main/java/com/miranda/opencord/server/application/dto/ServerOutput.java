package com.miranda.opencord.server.application.dto;

import java.util.UUID;

public record ServerOutput(
        UUID id,
        String name,
        String iconUrl,
        UUID ownerId
) {}