package com.miranda.opencord.call.infrastructure.controller;

import com.miranda.opencord.call.application.dto.GenerateRoomTokenCommand;
import com.miranda.opencord.call.application.dto.GenerateRoomTokenOutput;
import com.miranda.opencord.call.application.usecase.GenerateRoomTokenUseCase;
import com.miranda.opencord.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/calls")
@RequiredArgsConstructor
public class CallController {

    private final GenerateRoomTokenUseCase generateRoomTokenUseCase;


    @GetMapping("/{channelId}/token")
    public ResponseEntity<GenerateRoomTokenOutput> getToken(
            @PathVariable UUID channelId,
            @AuthenticationPrincipal UserEntity authenticatedUser) {

        return ResponseEntity.ok(generateRoomTokenUseCase.execute(
                new GenerateRoomTokenCommand(authenticatedUser.getId(),
                        channelId)
        ));
    }
}
