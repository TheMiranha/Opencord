package com.miranda.opencord.server.application.dto;


import java.util.UUID;

public record CreateServerCommand(
        String name,
        UUID ownerId
) {
}
