package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.application.dto.ServerInviteOutput;
import com.miranda.opencord.server.domain.ServerInviteEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.service.ServerInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetServerInvitesUseCase {

    private final ServerMemberRepository serverMemberRepository;
    private final ServerInviteService serverInviteService;

    @Transactional(readOnly = true)
    public List<ServerInviteOutput> execute(UUID serverId, UUID userId) {
        ServerMemberEntity member = serverMemberRepository.findByServerIdAndUserId(serverId, userId)
                .orElseThrow(() -> new RuntimeException("Você não é membro deste servidor"));

        if (!"ADMIN".equals(member.getRole())) {
            throw new RuntimeException("Apenas administradores podem visualizar os convites do servidor");
        }

        List<ServerInviteEntity> invites = serverInviteService.findAllByServerId(serverId);

        return invites.stream()
                .map(invite -> new ServerInviteOutput(
                        invite.getId(),
                        invite.getCode(),
                        invite.getServer().getId(),
                        invite.getInviter().getUsername(),
                        invite.getCreatedAt()
                ))
                .toList();
    }
}
