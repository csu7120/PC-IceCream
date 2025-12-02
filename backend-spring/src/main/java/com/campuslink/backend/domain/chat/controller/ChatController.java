package com.campuslink.backend.domain.chat.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.chat.dto.ChatMessageRequest;
import com.campuslink.backend.domain.chat.dto.ChatMessageResponse;
import com.campuslink.backend.domain.chat.dto.ChatRoomResponse;
import com.campuslink.backend.domain.chat.service.ChatService;
import com.campuslink.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;  // ⭐ 추가
import org.springframework.web.bind.annotation.RequestParam; // ⭐ 추가

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    private Integer currentUserId(Authentication authentication) {
        if (authentication == null) return null;
        var user = (CustomUserDetails) authentication.getPrincipal();
        return user.getUserId();
    }

    // -------------------------------
    // 1:1 채팅 생성
    // -------------------------------
    @PostMapping("/direct/{targetUserId}")
    public ApiResponse<ChatRoomResponse> openDirect(
            @PathVariable Integer targetUserId,
            Authentication authentication
    ) {
        Integer meId = currentUserId(authentication);
        return ApiResponse.ok(chatService.openDirectChat(meId, targetUserId));
    }

    // -------------------------------
    // 메시지 조회
    // -------------------------------
    @GetMapping("/{chatId}/messages")
    public ApiResponse<List<ChatMessageResponse>> getMessages(
            @PathVariable Integer chatId,
            Authentication authentication
    ) {
        Integer meId = currentUserId(authentication);
        return ApiResponse.ok(chatService.getMessages(chatId, meId));
    }

    // -------------------------------
    // 텍스트 메시지 전송
    // -------------------------------
    @PostMapping("/{chatId}/messages")
    public ApiResponse<ChatMessageResponse> sendMessage(
            @PathVariable Integer chatId,
            Authentication authentication,
            @RequestBody ChatMessageRequest req
    ) {
        Integer meId = currentUserId(authentication);
        return ApiResponse.ok(chatService.sendMessage(chatId, meId, req));
    }

    // -------------------------------
    // 이미지 업로드
    // -------------------------------
    @PostMapping("/upload")
    public ApiResponse<String> uploadImage(
            @RequestParam("image") MultipartFile file
    ) {
        try {
            String url = chatService.uploadImage(file);
            return ApiResponse.ok(url);
        } catch (Exception e) {
            return ApiResponse.fail("이미지 업로드 실패: " + e.getMessage());
        }
    }
    @GetMapping("/rooms")
    public ApiResponse<List<ChatRoomResponse>> getRooms(Authentication auth) {
        Integer meId = currentUserId(auth);
        return ApiResponse.ok(chatService.getMyChatRooms(meId));
    }


    
}
