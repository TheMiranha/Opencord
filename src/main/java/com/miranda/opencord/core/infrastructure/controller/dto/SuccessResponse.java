package com.miranda.opencord.core.infrastructure.controller.dto;

import java.time.Instant;

public record SuccessResponse<T>(
        Instant time,
        String requestId,
        T data
) {
}
