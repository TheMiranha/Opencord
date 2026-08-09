package com.miranda.opencord.channel.infrastructure.controller;

import com.miranda.opencord.channel.application.dto.ChannelOutput;
import com.miranda.opencord.channel.application.dto.GetDMChannelsCommand;
import com.miranda.opencord.channel.application.usecase.GetDMChannelsUseCase;
import com.miranda.opencord.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final GetDMChannelsUseCase getDMChannelsUseCase;

    @GetMapping("/dm")
    public ResponseEntity<List<ChannelOutput>> handleDMs(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(
                getDMChannelsUseCase.execute(new GetDMChannelsCommand(user.getId()))
        );
    }
}
