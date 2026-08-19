package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.channel.application.dto.ChannelWebSocketEvent;
import com.miranda.opencord.channel.application.dto.ServerChannelOutput;
import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.domain.ChannelType;
import com.miranda.opencord.channel.infrastructure.repository.ChannelRepository;
import com.miranda.opencord.server.application.dto.CreateServerChannelCommand;
import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerRepository;
import com.miranda.opencord.server.infrastructure.service.ServerPermissionService;
import com.miranda.opencord.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateServerChannelUseCase {

    private final ServerRepository serverRepository;
    private final ServerMemberRepository serverMemberRepository;
    private final ChannelRepository channelRepository;
    private final ServerPermissionService permissionService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public ServerChannelOutput execute(CreateServerChannelCommand command) {
        permissionService.validatePermission(command.serverId(), command.requesterId(), ServerPermission.MANAGE_CHANNELS);

        ServerEntity server = serverRepository.findById(command.serverId())
                .orElseThrow(() -> new RuntimeException("Servidor não encontrado"));

        String rawName = command.name() != null ? command.name().trim() : "novo-canal";
        String formattedName;

        if (command.type() == ChannelType.SERVER_TEXT) {
            formattedName = rawName.toLowerCase()
                    .replaceAll("\\s+", "-")
                    .replaceAll("[^a-z0-9-_áàâãéèêíïóôõöúçñ]", "");
            if (formattedName.isBlank()) {
                formattedName = "novo-canal";
            }
        } else {
            formattedName = rawName.isBlank() ? "Novo Canal de Voz" : rawName;
        }

        // Adiciona todos os membros atuais do servidor como membros do novo canal
        List<ServerMemberEntity> serverMembers = serverMemberRepository.findAllByServerId(server.getId());
        Set<UserEntity> channelMembers = new HashSet<>();

        if (server.getOwner() != null) {
            channelMembers.add(server.getOwner());
        }

        for (ServerMemberEntity member : serverMembers) {
            if (member.getUser() != null) {
                channelMembers.add(member.getUser());
            }
        }

        ChannelEntity channel = ChannelEntity.builder()
                .name(formattedName)
                .type(command.type())
                .server(server)
                .members(channelMembers)
                .build();

        ChannelEntity savedChannel = channelRepository.save(channel);

        ServerChannelOutput output = new ServerChannelOutput(
                savedChannel.getId(),
                savedChannel.getName(),
                savedChannel.getType(),
                server.getId()
        );

        // Notifica todos os membros conectados via WebSocket
        try {
            messagingTemplate.convertAndSend(
                    "/topic/server." + server.getId() + ".channels",
                    new ChannelWebSocketEvent("CHANNEL_CREATED", server.getId(), savedChannel.getId(), output)
            );
        } catch (Exception e) {
            log.error("Erro ao emitir evento WebSocket de canal criado: {}", e.getMessage());
        }

        return output;
    }
}
