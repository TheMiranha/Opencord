package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.application.dto.ServerRoleOutput;
import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.domain.ServerRoleEntity;
import com.miranda.opencord.server.infrastructure.controller.dto.UpdateServerRoleRequest;
import com.miranda.opencord.server.infrastructure.repository.ServerRoleRepository;
import com.miranda.opencord.server.infrastructure.service.ServerPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateServerRoleUseCase {

    private final ServerRoleRepository serverRoleRepository;
    private final ServerPermissionService permissionService;

    @Transactional
    public ServerRoleOutput execute(UUID serverId, UUID roleId, UpdateServerRoleRequest request, UUID requesterId) {
        permissionService.validatePermission(serverId, requesterId, ServerPermission.MANAGE_ROLES);

        ServerRoleEntity role = serverRoleRepository.findByIdAndServerId(roleId, serverId)
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        role.setName(request.name().trim());
        if (request.color() != null && !request.color().isBlank()) {
            role.setColor(request.color());
        }
        if (request.permissions() != null) {
            role.setPermissions(request.permissions());
        }

        ServerRoleEntity updated = serverRoleRepository.save(role);

        return new ServerRoleOutput(
                updated.getId(),
                serverId,
                updated.getName(),
                updated.getColor(),
                updated.getPosition(),
                updated.getPermissions()
        );
    }
}
