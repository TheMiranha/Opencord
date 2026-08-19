package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.application.dto.ServerBanOutput;
import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.infrastructure.repository.ServerBanRepository;
import com.miranda.opencord.server.infrastructure.service.ServerPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetServerBansUseCase {

    private final ServerBanRepository serverBanRepository;
    private final ServerPermissionService permissionService;

    @Transactional(readOnly = true)
    public List<ServerBanOutput> execute(UUID serverId, UUID requesterId) {
        permissionService.validatePermission(serverId, requesterId, ServerPermission.BAN_MEMBERS);

        return serverBanRepository.findAllByServerIdWithUsers(serverId).stream()
                .map(ban -> new ServerBanOutput(
                        ban.getId(),
                        ban.getUser().getId(),
                        ban.getUser().getUsername(),
                        ban.getUser().getAvatarUrl(),
                        ban.getReason(),
                        ban.getBannedBy().getUsername(),
                        ban.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
