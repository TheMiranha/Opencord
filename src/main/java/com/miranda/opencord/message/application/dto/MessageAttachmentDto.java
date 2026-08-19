package com.miranda.opencord.message.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageAttachmentDto(
        @JsonProperty("url") String url,
        @JsonProperty("name") String name,
        @JsonProperty("size") Long size,
        @JsonProperty("contentType") String contentType
) {}
