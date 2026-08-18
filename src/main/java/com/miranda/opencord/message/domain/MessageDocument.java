package com.miranda.opencord.message.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
public class MessageDocument {

    @Id
    String id;

    @Indexed
    UUID channelId;
    UUID senderId;
    String content;
    
    @CreatedDate
    Instant createdAt;

}
