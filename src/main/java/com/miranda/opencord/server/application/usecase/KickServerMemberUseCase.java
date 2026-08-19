package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.infrastructure.repository.ChannelRepository;
import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerRepository;
import com.miranda.opencord.server.infrastructure.service.ServerPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KickServerMemberUseCase {

    private final ServerRepository serverRepository;
    private final ServerMemberRepository serverMemberRepository;
    private final ChannelRepository channelRepository;
    private final ServerPermissionService permissionService;

    @Transactional
    public void execute(UUID serverId, UUID targetMemberId, UUID requesterId) {
        permissionService.validatePermission(serverId, requesterId, ServerPermission.KICK_MEMBERS);

        ServerEntity server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Servidor não encontrado"));

        ServerMemberEntity targetMember = serverMemberRepository.findById(targetMemberId)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado"));

        if (targetMember.getUser().getId().equals(server.getOwner().getId())) {
            throw new IllegalArgumentException("Não é possível expulsar o dono do servidor.");
        }

        if (targetMember.getUser().getId().equals(requesterId)) {
            throw new IllegalArgumentException("Você não pode expulsar a si mesmo.");
        }

        UUID targetUserId = targetMember.getUser().getId();

        // 1. Remove da tabela de membros do servidor
        serverMemberRepository.delete(targetMember);

        // 2. Remove o usuário de todos os canais do servidor
        List<ChannelEntity> serverChannels = channelRepository.findAllByServerId(serverId);
        for (ChannelEntity channel : serverChannels) {
            channel.getMembers().removeIf(user -> user.getId().equals(targetUserId));
            channelRepository.save(channel);
        }
    }
}
