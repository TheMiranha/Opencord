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

        Map<UUID, String> userMap = userRepository.findAllById(senderIds).stream()
                .collect(Collectors.toMap(UserEntity::getId, UserEntity::getUsername));

        List<MessageOutput> outputs = messagePage.getContent().stream()
                .map(doc -> MessageOutput.builder()
                        .id(doc.getId())
                        .channelId(doc.getChannelId())
                        .senderId(doc.getSenderId())
                        .senderUsername(userMap.getOrDefault(doc.getSenderId(), "Usuário"))
                        .content(doc.getContent())
                        .createdAt(doc.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());

        return new PageImpl<>(outputs, pageRequest, messagePage.getTotalElements());
    }
}
