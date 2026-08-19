package com.miranda.opencord.channel.application.usecase;

import com.miranda.opencord.call.domain.exception.IsNotAMember;
import com.miranda.opencord.channel.application.dto.GetChannelMessagesCommand;
import com.miranda.opencord.channel.application.dto.MessageOutput;
import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.message.infrastructure.service.MessageService;
import com.miranda.opencord.message.domain.MessageDocument;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetChannelMessagesUseCase {

    private final ChannelService channelService;
    private final MessageService messageService;
    private final UserRepository userRepository;

    public Page<MessageOutput> execute(GetChannelMessagesCommand command) {

        if (!channelService.isUserMemberOf(command.userId(), command.channelId())) {
            throw new IsNotAMember();
        }

        PageRequest pageRequest = PageRequest.of(command.page(), command.size());

        Page<MessageDocument> messagePage = messageService.findByChannelIdOrderByCreatedAtDesc(
                command.channelId(),
                pageRequest
        );

        Set<UUID> senderIds = messagePage.getContent().stream()
                .map(MessageDocument::getSenderId)
                .collect(Collectors.toSet());

        Map<UUID, UserEntity> userMap = userRepository.findAllById(senderIds).stream()
                .collect(Collectors.toMap(UserEntity::getId, u -> u));

        List<MessageOutput> outputs = messagePage.getContent().stream()
                .map(doc -> {
                    UserEntity sender = userMap.get(doc.getSenderId());
                    List<com.miranda.opencord.message.application.dto.MessageAttachmentDto> attDtos = doc.getAttachments() != null
                            ? doc.getAttachments().stream()
                            .filter(a -> a != null && a.getUrl() != null && !a.getUrl().isBlank())
                            .map(a -> new com.miranda.opencord.message.application.dto.MessageAttachmentDto(a.getUrl(), a.getName(), a.getSize(), a.getContentType()))
                            .toList()
                            : null;

                    if (attDtos != null && attDtos.isEmpty()) {
                        attDtos = null;
                    }

                    return MessageOutput.builder()
                            .id(doc.getId())
                            .channelId(doc.getChannelId())
                            .senderId(doc.getSenderId())
                            .senderUsername(sender != null ? sender.getUsername() : "Usuário")
                            .senderAvatarUrl(sender != null ? sender.getAvatarUrl() : null)
                            .content(doc.getContent())
                            .attachments(attDtos)
                            .createdAt(doc.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(outputs, pageRequest, messagePage.getTotalElements());
    }
}
