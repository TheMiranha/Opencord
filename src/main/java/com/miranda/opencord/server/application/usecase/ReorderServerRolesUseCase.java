package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.application.dto.ServerRoleOutput;
import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.domain.ServerRoleEntity;
import com.miranda.opencord.server.infrastructure.controller.dto.ReorderRolesRequest;
import com.miranda.opencord.server.infrastructure.repository.ServerRoleRepository;
import com.miranda.opencord.server.infrastructure.service.ServerPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReorderServerRolesUseCase {

    private final ServerRoleRepository serverRoleRepository;
    private final ServerPermissionService permissionService;

    @Transactional
    public List<ServerRoleOutput> execute(UUID serverId, ReorderRolesRequest request, UUID requesterId) {
        permissionService.validatePermission(serverId, requesterId, ServerPermission.MANAGE_ROLES);

        List<ServerRoleEntity> roles = serverRoleRepository.findAllByServerIdOrderByPositionAsc(serverId);
        Map<UUID, Integer> positionMap = request.roles().stream()
                .collect(Collectors.toMap(ReorderRolesRequest.RolePositionItem::id, ReorderRolesRequest.RolePositionItem::position));

        for (ServerRoleEntity role : roles) {
            if (positionMap.containsKey(role.getId())) {
                role.setPosition(positionMap.get(role.getId()));
                serverRoleRepository.save(role);
            }
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
