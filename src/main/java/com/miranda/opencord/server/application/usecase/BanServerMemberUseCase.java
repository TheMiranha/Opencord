package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.infrastructure.repository.ChannelRepository;
import com.miranda.opencord.server.domain.ServerBanEntity;
import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.infrastructure.controller.dto.BanMemberRequest;
import com.miranda.opencord.server.infrastructure.repository.ServerBanRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerRepository;
import com.miranda.opencord.server.infrastructure.service.ServerPermissionService;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import com.miranda.opencord.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BanServerMemberUseCase {

    private final ServerRepository serverRepository;
    private final ServerMemberRepository serverMemberRepository;
    private final ServerBanRepository serverBanRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ServerPermissionService permissionService;

    @Transactional
    public void execute(UUID serverId, UUID targetMemberId, BanMemberRequest request, UUID requesterId) {
        permissionService.validatePermission(serverId, requesterId, ServerPermission.BAN_MEMBERS);

        ServerEntity server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Servidor não encontrado"));

        ServerMemberEntity targetMember = serverMemberRepository.findById(targetMemberId)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado"));

        UserEntity targetUser = targetMember.getUser();
        UserEntity requester = userRepository.findById(requesterId).orElseThrow(UserNotFound::new);

        if (targetUser.getId().equals(server.getOwner().getId())) {
            throw new IllegalArgumentException("Não é possível banir o dono do servidor.");
        }

        if (targetUser.getId().equals(requesterId)) {
            throw new IllegalArgumentException("Você não pode banir a si mesmo.");
        }

        // Cria ou atualiza o banimento
        if (!serverBanRepository.existsByServerIdAndUserId(serverId, targetUser.getId())) {
            ServerBanEntity ban = ServerBanEntity.builder()
                    .server(server)
                    .user(targetUser)
                    .bannedBy(requester)
                    .reason(request != null && request.reason() != null ? request.reason().trim() : "Banido pelo moderador")
                    .build();
            serverBanRepository.save(ban);
        }

        // 1. Remove da tabela de membros do servidor
        serverMemberRepository.delete(targetMember);

        // 2. Remove o usuário de todos os canais do servidor
        List<ChannelEntity> serverChannels = channelRepository.findAllByServerId(serverId);
        for (ChannelEntity channel : serverChannels) {
            channel.getMembers().removeIf(user -> user.getId().equals(targetUser.getId()));
            channelRepository.save(channel);
        }
    }
}
