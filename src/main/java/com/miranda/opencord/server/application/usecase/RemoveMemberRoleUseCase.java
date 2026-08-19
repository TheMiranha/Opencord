package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.domain.ServerPermission;
import com.miranda.opencord.server.domain.ServerRoleEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerRoleRepository;
import com.miranda.opencord.server.infrastructure.service.ServerPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RemoveMemberRoleUseCase {

    private final ServerMemberRepository serverMemberRepository;
    private final ServerRoleRepository serverRoleRepository;
    private final ServerPermissionService permissionService;

    @Transactional
    public void execute(UUID serverId, UUID memberId, UUID roleId, UUID requesterId) {
        permissionService.validatePermission(serverId, requesterId, ServerPermission.MANAGE_ROLES);

        ServerMemberEntity member = serverMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado"));

        ServerRoleEntity role = serverRoleRepository.findByIdAndServerId(roleId, serverId)
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        if (member.getRoles() != null) {
            member.getRoles().remove(role);
            serverMemberRepository.save(member);
        }
    }
}
