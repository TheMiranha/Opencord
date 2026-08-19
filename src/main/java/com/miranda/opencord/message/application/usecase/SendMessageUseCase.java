package com.miranda.opencord.message.application.usecase;

import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.message.application.dto.SendMessageCommand;
import com.miranda.opencord.message.application.dto.SendMessageOutput;
import com.miranda.opencord.message.domain.MessageDocument;
import com.miranda.opencord.message.infrastructure.service.MessageService;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.infrastructure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendMessageUseCase {

    private final ChannelService channelService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserService userService;

    public void execute(SendMessageCommand command) {
        if (!channelService.isUserMemberOf(command.senderId(), command.channelId())) {
            throw new IllegalArgumentException("Você não pode enviar mensagens para este canal.");
        }

        UserEntity sender = userService.findById(command.senderId()).orElse(null);
        String senderUsername = sender != null ? sender.getUsername() : "Usuário";
        String senderAvatarUrl = sender != null ? sender.getAvatarUrl() : null;

        List<com.miranda.opencord.message.application.dto.MessageAttachmentDto> validAttachments = command.attachments() != null
                ? command.attachments().stream()
                .filter(a -> a != null && a.url() != null && !a.url().isBlank())
                .toList()
                : null;

        if (validAttachments != null && validAttachments.isEmpty()) {
            validAttachments = null;
        }

        List<com.miranda.opencord.message.domain.MessageAttachment> docAttachments = validAttachments != null
                ? validAttachments.stream()
                .map(a -> new com.miranda.opencord.message.domain.MessageAttachment(a.url(), a.name(), a.size(), a.contentType()))
                .toList()
                : null;

        MessageDocument savedMessage = messageService.save(MessageDocument.builder()
                .content(command.content())
                .channelId(command.channelId())
                .senderId(command.senderId())
                .attachments(docAttachments)
                .build());

        SendMessageOutput message = new SendMessageOutput(
                savedMessage.getId(),
                savedMessage.getSenderId(),
                senderUsername,
                senderAvatarUrl,
                savedMessage.getChannelId(),
                savedMessage.getContent(),
                validAttachments,
                savedMessage.getCreatedAt()
        );

        messagingTemplate.convertAndSend(
                "/topic/channel." + message.channelId(), message
        );
    }
}
