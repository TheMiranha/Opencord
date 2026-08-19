package com.miranda.opencord.message.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.miranda.opencord.message.application.dto.MessageAttachmentDto;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatMessageRequest(
        @JsonProperty("channelId") UUID channelId,
        @JsonProperty("content") String content,
        @JsonProperty("attachments") List<MessageAttachmentDto> attachments
) {}
