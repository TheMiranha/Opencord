package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.application.dto.ServerRoleOutput;
import com.miranda.opencord.server.domain.exception.IsNotAServerMember;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetServerRolesUseCase {

    private final ServerRoleRepository serverRoleRepository;
    private final ServerMemberRepository serverMemberRepository;

    @Transactional(readOnly = true)
    public List<ServerRoleOutput> execute(UUID serverId, UUID requesterId) {
        boolean isMember = serverMemberRepository.findByServerIdAndUserId(serverId, requesterId).isPresent();
        if (!isMember) {
            throw new IsNotAServerMember();
        }

        return serverRoleRepository.findAllByServerIdOrderByPositionAsc(serverId).stream()
                .map(role -> new ServerRoleOutput(
                        role.getId(),
                        serverId,
                        role.getName(),
                        role.getColor(),
                        role.getPosition(),
                        role.getPermissions()
                ))
                .collect(Collectors.toList());
    }
}
