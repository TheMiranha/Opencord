package com.miranda.opencord.channel.application.usecase;

import com.miranda.opencord.call.domain.exception.IsNotAMember;
import com.miranda.opencord.channel.application.dto.GetChannelMessagesCommand;
import com.miranda.opencord.channel.application.dto.MessageOutput;
import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.message.infrastructure.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetChannelMessagesUseCase {

    private final ChannelService channelService;
    private final MessageService messageService;

    public Page<MessageOutput> execute(GetChannelMessagesCommand command) {

        if (!channelService.isUserMemberOf(command.userId(), command.channelId())) {
            throw new IsNotAMember();
        }

        PageRequest pageRequest = PageRequest.of(command.page(), command.size());

        return messageService.findByChannelIdOrderByCreatedAtDesc(
                        command.channelId(),
                pageRequest
        )
                .map(doc -> MessageOutput.builder()
                                .id(doc.getId())
                                .channelId(doc.getChannelId())
                                .senderId(doc.getSenderId())
                                .content(doc.getContent())
                                .createdAt(doc.getCreatedAt())
                        .build()
                );
    }
}
