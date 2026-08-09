package com.miranda.opencord.channel.application.dto;

import java.util.UUID;

public record GetDMChannelsCommand(
        UUID userId
) {
}
