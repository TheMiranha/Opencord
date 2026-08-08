package com.miranda.opencord.core.infrastructure.controller.dto;

import java.time.Instant;

public record ErrorResponse(
        String message,
        Instant timestamp
) {
}
