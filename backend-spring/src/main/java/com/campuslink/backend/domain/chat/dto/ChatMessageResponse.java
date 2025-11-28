package com.campuslink.backend.domain.chat.dto;

import com.campuslink.backend.domain.chat.entity.MessageType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageResponse {

    private Integer messageId;
    private Integer chatId;
    private Integer senderId;
    private MessageType messageType;
    private String content;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String locationLabel;
    private LocalDateTime sentAt;
    private boolean read;
}
