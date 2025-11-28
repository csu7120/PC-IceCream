package com.campuslink.backend.domain.chat.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.chat.dto.ChatMessageResponse;
import com.campuslink.backend.domain.chat.dto.ChatRoomResponse;
import com.campuslink.backend.domain.chat.dto.ChatMessageRequest;
import com.campuslink.backend.domain.chat.service.ChatService;
import com.campuslink.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private Integer currentUserId(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUserId();
    }

    // 1:1 채팅방 생성 or 기존 방 반환
    @PostMapping("/direct/{targetUserId}")
    public ApiResponse<ChatRoomResponse> openDirectChat(
            @PathVariable Integer targetUserId,
            Authentication authentication
    ) {
        Integer meId = currentUserId(authentication);
        ChatRoomResponse room = chatService.openDirectChat(meId, targetUserId);
        return ApiResponse.ok(room);
    }

    // 특정 방 메시지 조회
    @GetMapping("/{chatId}/messages")
    public ApiResponse<List<ChatMessageResponse>> getMessages(
            @PathVariable Integer chatId,
            Authentication authentication
    ) {
        Integer meId = currentUserId(authentication);
        List<ChatMessageResponse> msgs = chatService.getMessages(chatId, meId);
        return ApiResponse.ok(msgs);
    }

    // 메시지 보내기
    @PostMapping("/{chatId}/messages")
    public ApiResponse<ChatMessageResponse> sendMessage(
            @PathVariable Integer chatId,
            @RequestBody ChatMessageRequest req,
            Authentication authentication
    ) {
        Integer meId = currentUserId(authentication);
        ChatMessageResponse msg = chatService.sendMessage(chatId, meId, req);
        return ApiResponse.ok(msg);
    }
}
