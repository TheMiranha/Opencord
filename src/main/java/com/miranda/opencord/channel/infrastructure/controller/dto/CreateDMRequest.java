package com.miranda.opencord.channel.infrastructure.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDMRequest(
        @NotNull
        UUID recipientId
) {
}
