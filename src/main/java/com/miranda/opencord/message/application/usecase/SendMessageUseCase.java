package com.miranda.opencord.message.application.usecase;

import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.message.application.dto.SendMessageCommand;
import com.miranda.opencord.message.application.dto.SendMessageOutput;
import com.miranda.opencord.message.domain.MessageDocument;
import com.miranda.opencord.message.infrastructure.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendMessageUseCase {

    private final ChannelService channelService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public void execute(SendMessageCommand command) {
        if (!channelService.isUserMemberOf(command.senderId(), command.channelId())) {
            throw new IllegalArgumentException("Você não pode enviar mensagens para este canal.");
        }

        MessageDocument savedMessage = messageService.save(MessageDocument.builder()
                .content(command.content())
                .channelId(command.channelId())
                .senderId(command.senderId())
                .build());

        SendMessageOutput message = new SendMessageOutput(
                savedMessage.getId(),
                savedMessage.getSenderId(),
                savedMessage.getChannelId(),
                savedMessage.getContent(),
                savedMessage.getCreatedAt()
        );

        messagingTemplate.convertAndSend(
                "/topic/channel." + message.channelId(), message
        );
    }
}
