package com.miranda.opencord.user.infrastructure.controller.dto;

import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(
        @Size(max = 1000, message = "A biografia pode ter no máximo 1000 caracteres")
        String bio,

        @Size(max = 255, message = "O status personalizado pode ter no máximo 255 caracteres")
        String customStatus
) {
}
