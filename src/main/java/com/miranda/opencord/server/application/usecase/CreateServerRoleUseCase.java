package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.application.dto.ServerRoleOutput;
import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.domain.ServerRoleEntity;
import com.miranda.opencord.server.infrastructure.controller.dto.CreateServerRoleRequest;
import com.miranda.opencord.server.infrastructure.repository.ServerRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerRoleRepository;
import com.miranda.opencord.server.infrastructure.service.ServerPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateServerRoleUseCase {

    private final ServerRepository serverRepository;
    private final ServerRoleRepository serverRoleRepository;
    private final ServerPermissionService permissionService;

    @Transactional
    public ServerRoleOutput execute(UUID serverId, CreateServerRoleRequest request, UUID requesterId) {
        permissionService.validatePermission(serverId, requesterId, ServerPermission.MANAGE_ROLES);

        ServerEntity server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Servidor não encontrado"));

        Integer maxPosition = serverRoleRepository.findMaxPositionByServerId(serverId);

        ServerRoleEntity role = ServerRoleEntity.builder()
                .server(server)
                .name(request.name().trim())
                .color(request.color() != null && !request.color().isBlank() ? request.color() : "#99aab5")
                .position(maxPosition + 1)
                .permissions(request.permissions() != null ? request.permissions() : 0L)
                .build();

        ServerRoleEntity saved = serverRoleRepository.save(role);

        return new ServerRoleOutput(
                saved.getId(),
                serverId,
                saved.getName(),
                saved.getColor(),
                saved.getPosition(),
                saved.getPermissions()
        );
    }
}
