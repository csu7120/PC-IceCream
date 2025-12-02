package com.campuslink.backend.domain.chat.controller;

import com.campuslink.backend.domain.chat.dto.ChatMessageRequest;
import com.campuslink.backend.domain.chat.dto.ChatMessageResponse;
import com.campuslink.backend.domain.chat.service.ChatService;
import com.campuslink.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 클라이언트 → 서버 메시지 수신
     * /app/chat/{chatId} 경로로 메시지가 오면 실행됨
     */
    @MessageMapping("/chat/{chatId}")
    public void sendMessage(
            @DestinationVariable Integer chatId,
            ChatMessageRequest request,
            @Header("Authorization") String token,
            Authentication authentication
    ) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Integer senderId = user.getUserId();

        // 1) DB 저장 + 메시지 정보 생성
        ChatMessageResponse saved = chatService.sendMessage(chatId, senderId, request);

        // 2) 해당 채팅방 구독자들에게 메시지 전송
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, saved);
    }
}
