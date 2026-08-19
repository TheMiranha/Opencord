package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.application.dto.ServerOutput;
import com.miranda.opencord.server.infrastructure.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetMyServersUseCase {

    private final ServerService serverService;

    public List<ServerOutput> execute(UUID userId) {
        return serverService.findServersByUserId(userId).stream()
                .map(server -> new ServerOutput(
                        server.getId(),
                        server.getName(),
                        server.getIconUrl(),
                        server.getOwner().getId()
                ))
                .collect(Collectors.toList());
    }
}