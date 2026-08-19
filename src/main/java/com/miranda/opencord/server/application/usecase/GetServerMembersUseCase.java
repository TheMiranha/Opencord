package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.application.dto.ServerMemberOutput;
import com.miranda.opencord.server.application.dto.ServerRoleOutput;
import com.miranda.opencord.server.domain.ServerRoleEntity;
import com.miranda.opencord.server.domain.exception.IsNotAServerMember;
import com.miranda.opencord.server.infrastructure.service.ServerMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetServerMembersUseCase {

    private final ServerMemberService serverMemberService;

    @Transactional(readOnly = true)
    public List<ServerMemberOutput> execute(UUID serverId, UUID requesterUserId) {
        boolean isMember = serverMemberService.findByServerIdAndUserId(serverId, requesterUserId).isPresent();
        if (!isMember) {
            throw new IsNotAServerMember();
        }

        return serverMemberService.findAllByServerId(serverId).stream()
                .map(sm -> {
                    List<ServerRoleOutput> roleOutputs = sm.getRoles() != null
                            ? sm.getRoles().stream()
                                    .sorted(Comparator.comparingInt(r -> r.getPosition() != null ? r.getPosition() : 0))
                                    .map(r -> new ServerRoleOutput(
                                            r.getId(),
                                            serverId,
                                            r.getName(),
                                            r.getColor(),
                                            r.getPosition(),
                                            r.getPermissions()
                                    ))
                                    .collect(Collectors.toList())
                            : Collections.emptyList();

                    return new ServerMemberOutput(
                            sm.getId(),
                            sm.getUser().getId(),
                            sm.getUser().getUsername(),
                            sm.getUser().getAvatarUrl(),
                            sm.getRole(),
                            roleOutputs
                    );
                })
                .collect(Collectors.toList());
    }
}
