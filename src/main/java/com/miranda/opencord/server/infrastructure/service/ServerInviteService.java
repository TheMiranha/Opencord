package com.miranda.opencord.server.infrastructure.service;

import com.miranda.opencord.server.domain.ServerInviteEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerInviteService {

    private final ServerInviteRepository serverInviteRepository;

    public ServerInviteEntity save(ServerInviteEntity serverInvite) {
        return serverInviteRepository.save(serverInvite);
    }

    public Optional<ServerInviteEntity> findByCode(String code) {
        return serverInviteRepository.findByCode(code);
    }

    public List<ServerInviteEntity> findAllByServerId(UUID serverId) {
        return serverInviteRepository.findAllByServerIdOrderByCreatedAtDesc(serverId);
    }

    public Optional<ServerInviteEntity> findByServerIdAndCode(UUID serverId, String code) {
        return serverInviteRepository.findByServerIdAndCode(serverId, code);
    }

    public void delete(ServerInviteEntity serverInvite) {
        serverInviteRepository.delete(serverInvite);
    }
}
