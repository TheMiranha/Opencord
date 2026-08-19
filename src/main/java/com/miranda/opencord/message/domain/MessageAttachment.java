package com.miranda.opencord.message.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageAttachment {
    private String url;
    private String name;
    private Long size;
    private String contentType;
}
