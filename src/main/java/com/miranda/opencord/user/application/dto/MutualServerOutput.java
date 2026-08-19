package com.miranda.opencord.user.application.dto;

import java.util.UUID;

public record MutualServerOutput(
        UUID id,
        String name,
        String iconUrl
) {
}
