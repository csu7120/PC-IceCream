package com.campuslink.backend.domain.chat.dto;

import com.campuslink.backend.domain.chat.entity.MessageType;
import lombok.Data;

@Data
public class ChatMessageRequest {

    private MessageType messageType;  // TEXT / IMAGE / LOCATION
    private String content;           // 텍스트 내용 또는 설명
    private String imageUrl;          // 이미지 메시지일 때
    private Double latitude;          // 위치 메시지일 때
    private Double longitude;
    private String locationLabel;
}
