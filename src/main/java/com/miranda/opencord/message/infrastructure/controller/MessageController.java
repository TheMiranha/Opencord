package com.miranda.opencord.message.infrastructure.controller;

import com.miranda.opencord.channel.application.dto.GetChannelMessagesCommand;
import com.miranda.opencord.channel.application.dto.MessageOutput;
import com.miranda.opencord.channel.application.usecase.GetChannelMessagesUseCase;
import com.miranda.opencord.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/channels/{channelId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final GetChannelMessagesUseCase getChannelMessagesUseCase;

    @GetMapping
    public ResponseEntity<Page<MessageOutput>> getMessages(
            @PathVariable UUID channelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticationPrincipal UserEntity authenticatedUser) {

        Page<MessageOutput> messages = getChannelMessagesUseCase.execute(
                new GetChannelMessagesCommand(
                        authenticatedUser.getId(),
                        channelId,
                        page,
                        size
                )
        );

        return ResponseEntity.ok(messages);
    }
}