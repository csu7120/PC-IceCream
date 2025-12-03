package com.campuslink.backend.domain.chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {
    private Integer chatId;
    private String otherUserName;   // 상대 이름
    private String lastMessage;  
    private LocalDateTime lastMessageTime;
    public ChatRoomResponse(Integer chatId) {
        this.chatId = chatId;
    }// 마지막 메시지
}
