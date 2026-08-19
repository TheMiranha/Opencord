package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.infrastructure.repository.ChannelRepository;
import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LeaveServerUseCase {

    private final ServerRepository serverRepository;
    private final ServerMemberRepository serverMemberRepository;
    private final ChannelRepository channelRepository;

    @Transactional
    public void execute(UUID serverId, UUID userId) {
        ServerEntity server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Servidor não encontrado"));

        if (server.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("O dono do servidor não pode sair sem antes transferir ou deletar o servidor.");
        }

        ServerMemberEntity member = serverMemberRepository.findByServerIdAndUserId(serverId, userId)
                .orElseThrow(() -> new RuntimeException("Você não é membro deste servidor."));

        // 1. Remove da tabela de membros
        serverMemberRepository.delete(member);

        // 2. Remove o usuário de todos os canais do servidor
        List<ChannelEntity> serverChannels = channelRepository.findAllByServerId(serverId);
        for (ChannelEntity channel : serverChannels) {
            channel.getMembers().removeIf(u -> u.getId().equals(userId));
            channelRepository.save(channel);
        }
    }
}
