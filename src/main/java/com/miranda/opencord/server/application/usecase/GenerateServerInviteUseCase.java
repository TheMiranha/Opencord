package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.application.dto.GenerateInviteOutput;
import com.miranda.opencord.server.domain.ServerInviteEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerInviteRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GenerateServerInviteUseCase {

    private final ServerMemberRepository serverMemberRepository;
    private final ServerInviteRepository serverInviteRepository;

    @Transactional
    public GenerateInviteOutput execute(UUID serverId, UUID userId) {

        ServerMemberEntity member = serverMemberRepository.findByServerIdAndUserId(serverId, userId)
                .orElseThrow(() -> new RuntimeException("Você não é membro deste servidor"));

        if (!"ADMIN".equals(member.getRole())) {
            throw new RuntimeException("Apenas administradores podem gerar convites");
        }

        String code = UUID.randomUUID().toString().substring(0, 8);

        ServerInviteEntity invite = ServerInviteEntity.builder()
                .code(code)
                .server(member.getServer())
                .inviter(member.getUser())
                .build();

        serverInviteRepository.save(invite);

        return new GenerateInviteOutput(code, serverId);
    }
}