package com.miranda.opencord.message.infrastructure.controller;

import com.miranda.opencord.channel.application.dto.GetChannelMessagesCommand;
import com.miranda.opencord.channel.application.dto.MessageOutput;
import com.miranda.opencord.channel.application.usecase.GetChannelMessagesUseCase;
import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.message.application.dto.MessageAttachmentDto;
import com.miranda.opencord.storage.infrastructure.service.MinioStorageService;
import com.miranda.opencord.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/channels/{channelId}")
@RequiredArgsConstructor
public class MessageController {

    private final GetChannelMessagesUseCase getChannelMessagesUseCase;
    private final ChannelService channelService;
    private final MinioStorageService minioStorageService;

    @GetMapping("/messages")
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

    @PostMapping(value = "/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageAttachmentDto> uploadAttachment(
            @PathVariable UUID channelId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserEntity authenticatedUser) {

        if (!channelService.isUserMemberOf(authenticatedUser.getId(), channelId)) {
            throw new IllegalArgumentException("Você não possui permissão para enviar arquivos neste canal.");
        }

        MessageAttachmentDto attachment = minioStorageService.uploadAttachment(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(attachment);
    }
}