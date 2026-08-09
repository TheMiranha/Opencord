package com.miranda.opencord.message.infrastructure.controller;

import com.miranda.opencord.message.application.dto.SendMessageCommand;
import com.miranda.opencord.message.application.usecase.SendMessageUseCase;
import com.miranda.opencord.message.infrastructure.controller.dto.ChatMessageRequest;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SendMessageUseCase sendMessageUseCase;

    @MessageMapping("/chat.send")
    public void handleSendMessage(@Payload ChatMessageRequest incomingMessage, Principal principal) {

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
        UserEntity sender = (UserEntity) auth.getDetails();

        if (sender == null) {
            throw new UserNotFound();
        }

        sendMessageUseCase.execute(new SendMessageCommand(
                sender.getId(),
                incomingMessage.channelId(),
                incomingMessage.content()
        ));
    }

}
