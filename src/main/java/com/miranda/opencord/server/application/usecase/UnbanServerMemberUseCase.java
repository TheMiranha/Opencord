package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.infrastructure.repository.ServerBanRepository;
import com.miranda.opencord.server.infrastructure.service.ServerPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UnbanServerMemberUseCase {

    private final ServerBanRepository serverBanRepository;
    private final ServerPermissionService permissionService;

    @Transactional
    public void execute(UUID serverId, UUID targetUserId, UUID requesterId) {
        permissionService.validatePermission(serverId, requesterId, ServerPermission.BAN_MEMBERS);

        serverBanRepository.deleteByServerIdAndUserId(serverId, targetUserId);
    }
}
