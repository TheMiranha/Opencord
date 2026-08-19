package com.miranda.opencord.server.infrastructure.service;

import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerMemberService {

    private final ServerMemberRepository serverMemberRepository;

    public ServerMemberEntity save(ServerMemberEntity serverMember) {
        return serverMemberRepository.save(serverMember);
    }

    public Optional<ServerMemberEntity> findByServerIdAndUserId(UUID serverId, UUID userId) {
        return serverMemberRepository.findByServerIdAndUserId(serverId, userId);
    }

    public java.util.List<ServerMemberEntity> findAllByServerId(UUID serverId) {
        return serverMemberRepository.findAllByServerId(serverId);
    }
}
