package com.campuslink.backend.domain.chat.service;

import com.campuslink.backend.domain.chat.dto.ChatMessageResponse;
import com.campuslink.backend.domain.chat.dto.ChatRoomResponse;
import com.campuslink.backend.domain.chat.dto.ChatMessageRequest;
import com.campuslink.backend.domain.chat.entity.*;
import com.campuslink.backend.domain.chat.repository.*;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRooms;
    private final ChatMemberRepository chatMembers;
    private final ChatMessageRepository messages;
    private final UserRepository users;

    private User getUser(Integer userId) {
        return users.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    // 프로필에서 "채팅 보내기" 눌렀을 때
    public ChatRoomResponse openDirectChat(Integer meId, Integer targetUserId) {
        if (meId.equals(targetUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신과는 채팅을 만들 수 없습니다.");
        }

        var existing = chatRooms.findDirectChatBetween(meId, targetUserId)
                .orElse(null);

        if (existing != null) {
            return new ChatRoomResponse(existing.getId());
        }

        User me = getUser(meId);
        User target = getUser(targetUserId);

        ChatRoom room = ChatRoom.builder().build();
        room = chatRooms.save(room);

        ChatMember m1 = ChatMember.builder()
                .chat(room)
                .user(me)
                .build();

        ChatMember m2 = ChatMember.builder()
                .chat(room)
                .user(target)
                .build();

        chatMembers.save(m1);
        chatMembers.save(m2);

        return new ChatRoomResponse(room.getId());
    }

    // 방 메시지 전체 조회
    public List<ChatMessageResponse> getMessages(Integer chatId, Integer meId) {
        // 방 참여자인지 확인
        chatMembers.findByChat_IdAndUser_UserId(chatId, meId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.FORBIDDEN, "채팅방에 참여한 사용자만 조회할 수 있습니다."));

        return messages.findByChat_IdOrderBySentAtAsc(chatId).stream()
                .map(this::toMessageResponse)
                .toList();
    }

    // 메시지 보내기
    public ChatMessageResponse sendMessage(Integer chatId, Integer meId, ChatMessageRequest req) {
        ChatMember member = chatMembers.findByChat_IdAndUser_UserId(chatId, meId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.FORBIDDEN, "채팅방에 참여한 사용자만 메시지를 보낼 수 있습니다."));

        ChatRoom room = member.getChat();
        User sender = member.getUser();

        MessageType type = (req.getMessageType() != null) ? req.getMessageType() : MessageType.TEXT;

        ChatMessage msg = ChatMessage.builder()
                .chat(room)
                .sender(sender)
                .messageType(type)
                .content(req.getContent())
                .imageUrl(req.getImageUrl())
                .latitude(req.getLatitude() != null ? BigDecimal.valueOf(req.getLatitude()) : null)
                .longitude(req.getLongitude() != null ? BigDecimal.valueOf(req.getLongitude()) : null)
                .locationLabel(req.getLocationLabel())
                .read(false)
                .build();

        msg = messages.save(msg);

        return toMessageResponse(msg);
    }

    private ChatMessageResponse toMessageResponse(ChatMessage m) {
        return ChatMessageResponse.builder()
                .messageId(m.getId())
                .chatId(m.getChat().getId())
                .senderId(m.getSender().getUserId())
                .messageType(m.getMessageType())
                .content(m.getContent())
                .imageUrl(m.getImageUrl())
                .latitude(m.getLatitude() != null ? m.getLatitude().doubleValue() : null)
                .longitude(m.getLongitude() != null ? m.getLongitude().doubleValue() : null)
                .locationLabel(m.getLocationLabel())
                .sentAt(m.getSentAt())
                .read(m.isRead())
                .build();
    }
}
