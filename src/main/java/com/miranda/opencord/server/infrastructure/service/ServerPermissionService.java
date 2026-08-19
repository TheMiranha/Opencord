package com.miranda.opencord.server.infrastructure.service;

import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.domain.ServerRoleEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServerPermissionService {

    private final ServerRepository serverRepository;
    private final ServerMemberRepository serverMemberRepository;

    public boolean hasPermission(UUID serverId, UUID userId, ServerPermission requiredPermission) {
        ServerEntity server = serverRepository.findById(serverId).orElse(null);
        if (server == null) return false;

        // O dono do servidor tem todas as permissões
        if (server.getOwner().getId().equals(userId)) {
            return true;
        }

        ServerMemberEntity member = serverMemberRepository.findByServerIdAndUserId(serverId, userId).orElse(null);
        if (member == null) {
            return false;
        }

        // Suporte a legado: cargo ADMIN tem permissão irrestrita
        if ("ADMIN".equalsIgnoreCase(member.getRole())) {
            return true;
        }

        // Verifica os cargos atribuídos ao membro
        if (member.getRoles() != null) {
            for (ServerRoleEntity role : member.getRoles()) {
                if (ServerPermission.hasPermission(role.getPermissions(), requiredPermission)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void validatePermission(UUID serverId, UUID userId, ServerPermission requiredPermission) {
        if (!hasPermission(serverId, userId, requiredPermission)) {
            throw new AccessDeniedException("Você não possui permissão para realizar esta ação neste servidor.");
        }
    }
}
