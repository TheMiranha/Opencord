package com.miranda.opencord.message.infrastructure.repository;

import com.miranda.opencord.message.domain.MessageDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MessageRepository extends MongoRepository<MessageDocument, String> {

    Page<MessageDocument> findByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);
}
