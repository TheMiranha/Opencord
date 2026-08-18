package com.miranda.opencord.message.infrastructure.service;

import com.miranda.opencord.message.domain.MessageDocument;
import com.miranda.opencord.message.infrastructure.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageDocument save(MessageDocument message) {
        return messageRepository.save(message);
    }

    public Page<MessageDocument> findByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable) {
        return messageRepository.findByChannelIdOrderByCreatedAtDesc(
                channelId,
                pageable
        );
    }
}
