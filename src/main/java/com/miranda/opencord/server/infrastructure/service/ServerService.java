package com.miranda.opencord.server.infrastructure.service;

import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;

    public ServerEntity save(ServerEntity server) {
        return serverRepository.save(server);
    }

    public List<ServerEntity> findServersByUserId(UUID userId) {
        return serverRepository.findServersByUserId(userId);
    }
}
