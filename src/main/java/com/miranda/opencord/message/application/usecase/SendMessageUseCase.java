package com.miranda.opencord.message.application.usecase;

import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.message.application.dto.SendMessageCommand;
import com.miranda.opencord.message.application.dto.SendMessageOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendMessageUseCase {

    private final ChannelService channelService;
    private final SimpMessagingTemplate messagingTemplate;

    public void execute(SendMessageCommand command) {
        if (!channelService.isUserMemberOf(command.senderId(), command.channelId())) {
            throw new IllegalArgumentException("Você não pode enviar mensagens para este canal.");
        }

        SendMessageOutput message = new SendMessageOutput(
                command.senderId(),
                command.channelId(),
                command.content()
        );

        messagingTemplate.convertAndSend(
                "/topic/channel." + message.channelId(), message
        );
    }
}
