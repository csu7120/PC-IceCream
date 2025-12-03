package com.campuslink.backend.domain.chat.dto;

import com.campuslink.backend.domain.chat.entity.ChatMessage;
import com.campuslink.backend.domain.chat.entity.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {

    private Integer messageId;
    private Integer chatId;
    private Integer senderId;

    // TEXT 메시지
    private String content;

    // IMAGE 메시지
    private String imageUrl;
    private String lastMessageTime;
    // LOCATION 메시지
    private Double latitude;
    private Double longitude;
    private String locationLabel;

    private MessageType messageType;
    private LocalDateTime sentAt;

    public static ChatMessageResponse from(ChatMessage m) {
        return ChatMessageResponse.builder()
                .messageId(m.getId())
                .chatId(m.getChatId())
                .senderId(m.getSenderId())
                .content(m.getContent())
                .imageUrl(m.getImageUrl())
                .latitude(m.getLatitude())
                .longitude(m.getLongitude())
                .locationLabel(m.getLocationLabel())
                .messageType(m.getMessageType())
                .sentAt(m.getSentAt())
                .build();
    }
}
