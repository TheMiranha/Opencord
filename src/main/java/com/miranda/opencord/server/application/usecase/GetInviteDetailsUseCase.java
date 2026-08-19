package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.server.application.dto.InviteDetailsOutput;
import com.miranda.opencord.server.domain.ServerInviteEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerInviteRepository;
import com.miranda.opencord.server.infrastructure.service.ServerInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetInviteDetailsUseCase {

    private final ServerInviteService serverInviteService;

    public InviteDetailsOutput execute(String code) {
        ServerInviteEntity invite = serverInviteService.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Convite inválido ou expirado"));

        return new InviteDetailsOutput(
                invite.getCode(),
                invite.getServer().getId(),
                invite.getServer().getName(),
                invite.getInviter().getUsername()
        );
    }
}