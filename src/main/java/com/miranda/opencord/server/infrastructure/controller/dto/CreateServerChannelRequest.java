package com.miranda.opencord.server.infrastructure.controller.dto;

import com.miranda.opencord.channel.domain.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateServerChannelRequest(
        @NotBlank(message = "O nome do canal é obrigatório")
        @Size(min = 1, max = 100, message = "O nome do canal deve ter entre 1 e 100 caracteres")
        String name,

        @NotNull(message = "O tipo do canal é obrigatório")
        ChannelType type
) {
}
