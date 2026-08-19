package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.channel.application.dto.ChannelWebSocketEvent;
import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.infrastructure.repository.ChannelRepository;
import com.miranda.opencord.message.domain.MessageAttachment;
import com.miranda.opencord.message.domain.MessageDocument;
import com.miranda.opencord.message.infrastructure.repository.MessageRepository;
import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.infrastructure.repository.ServerRepository;
import com.miranda.opencord.server.infrastructure.service.ServerPermissionService;
import com.miranda.opencord.storage.infrastructure.service.MinioStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteServerChannelUseCase {

    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final MinioStorageService minioStorageService;
    private final ServerPermissionService permissionService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void execute(UUID serverId, UUID channelId, UUID requesterId) {
        permissionService.validatePermission(serverId, requesterId, ServerPermission.MANAGE_CHANNELS);

        ServerEntity server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Servidor não encontrado"));

        ChannelEntity channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Canal não encontrado"));

        if (channel.getServer() == null || !channel.getServer().getId().equals(serverId)) {
            throw new IllegalArgumentException("O canal informado não pertence a este servidor.");
        }

        // 1. Busca e deleta os anexos do canal no MinIO
        List<MessageDocument> messages = messageRepository.findAllByChannelId(channelId);
        for (MessageDocument msg : messages) {
            if (msg.getAttachments() != null) {
                for (MessageAttachment attachment : msg.getAttachments()) {
                    if (attachment != null && attachment.getUrl() != null) {
                        minioStorageService.deleteFileByUrl(attachment.getUrl());
                    }
                }
            }
        }

        // 2. Deleta as mensagens do canal no MongoDB
        if (!messages.isEmpty()) {
            messageRepository.deleteAll(messages);
        }

        // 3. Deleta o canal no PostgreSQL
        channelRepository.delete(channel);

        // 4. Notifica todos os membros conectados via WebSocket
        try {
            messagingTemplate.convertAndSend(
                    "/topic/server." + serverId + ".channels",
                    new ChannelWebSocketEvent("CHANNEL_DELETED", serverId, channelId, null)
            );
        } catch (Exception e) {
            log.error("Erro ao emitir evento WebSocket de canal deletado: {}", e.getMessage());
        }
    }
}
