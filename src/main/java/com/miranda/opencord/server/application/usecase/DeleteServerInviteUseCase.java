package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.domain.ServerInviteEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.service.ServerInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteServerInviteUseCase {

    private final ServerMemberRepository serverMemberRepository;
    private final ServerInviteService serverInviteService;

    @Transactional
    public void execute(UUID serverId, String code, UUID userId) {
        ServerMemberEntity member = serverMemberRepository.findByServerIdAndUserId(serverId, userId)
                .orElseThrow(() -> new RuntimeException("Você não é membro deste servidor"));

        if (!"ADMIN".equals(member.getRole())) {
            throw new RuntimeException("Apenas administradores podem deletar convites do servidor");
        }

        ServerInviteEntity invite = serverInviteService.findByServerIdAndCode(serverId, code)
                .orElseThrow(() -> new RuntimeException("Convite não encontrado"));

        serverInviteService.delete(invite);
    }
}
