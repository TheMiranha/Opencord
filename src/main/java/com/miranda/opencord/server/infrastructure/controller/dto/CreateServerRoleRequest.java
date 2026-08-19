package com.miranda.opencord.server.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateServerRoleRequest(
        @NotBlank(message = "O nome do cargo não pode estar em branco")
        @Size(max = 100, message = "O nome do cargo deve ter no máximo 100 caracteres")
        String name,

        String color,

        Long permissions
) {}
