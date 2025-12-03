package com.campuslink.backend.domain.chat.service;

import com.campuslink.backend.domain.chat.dto.ChatMessageRequest;
import com.campuslink.backend.domain.chat.dto.ChatMessageResponse;
import com.campuslink.backend.domain.chat.dto.ChatRoomResponse;
import com.campuslink.backend.domain.chat.entity.ChatMessage;
import com.campuslink.backend.domain.chat.entity.ChatRoom;
import com.campuslink.backend.domain.chat.entity.MessageType;
import com.campuslink.backend.domain.chat.repository.ChatMessageRepository;
import com.campuslink.backend.domain.chat.repository.ChatRoomRepository;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRooms;
    private final ChatMessageRepository messages;
    private final UserRepository users;

    // =============================
    // 유저 검증
    // =============================
    private User getUser(Integer id) {
        return users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 없음"));
    }

    // =============================
    // 1:1 프로필 채팅 생성
    // =============================
    public ChatRoomResponse openDirectChat(Integer meId, Integer targetId) {

        if (meId.equals(targetId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신과는 채팅 불가");
        }

        var existing = chatRooms.findByUser1IdAndUser2Id(meId, targetId)
                .or(() -> chatRooms.findByUser2IdAndUser1Id(meId, targetId))
                .orElse(null);

        if (existing != null) {
            return convertRoom(existing, meId);
        }

        ChatRoom room = ChatRoom.builder()
                .user1Id(meId)
                .user2Id(targetId)
                .createdAt(LocalDateTime.now())
                .build();

        room = chatRooms.save(room);

        return convertRoom(room, meId);
    }

    // =============================
    // 메시지 조회
    // =============================
    public List<ChatMessageResponse> getMessages(Integer chatId, Integer meId) {

        ChatRoom room = chatRooms.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "방 없음"));

        if (!isParticipant(room, meId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "참여자가 아님");

        return messages.findByChatIdOrderBySentAtAsc(chatId)
                .stream()
                .map(this::toMessageResponse)
                .toList();
    }

    private boolean isParticipant(ChatRoom r, Integer meId) {
        return (r.getUser1Id() != null && r.getUser1Id().equals(meId)) ||
                (r.getUser2Id() != null && r.getUser2Id().equals(meId)) ||
                (r.getLenderId() != null && r.getLenderId().equals(meId)) ||
                (r.getRenterId() != null && r.getRenterId().equals(meId));
    }

    // =============================
    // 메시지 전송
    // =============================
    public ChatMessageResponse sendMessage(Integer chatId, Integer senderId, ChatMessageRequest req) {

        ChatRoom room = chatRooms.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "방 없음"));

        if (!isParticipant(room, senderId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "참여자가 아님");

        ChatMessage msg = ChatMessage.builder()
                .chatId(chatId)
                .senderId(senderId)
                .messageType(req.getMessageType() != null ? req.getMessageType() : MessageType.TEXT)
                .content(req.getContent())
                .imageUrl(req.getImageUrl())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .locationLabel(req.getLocationLabel())
                .sentAt(LocalDateTime.now())
                .build();

        messages.save(msg);
        return toMessageResponse(msg);
    }

    // =============================
    // 이미지 업로드
    // =============================
    public String uploadImage(MultipartFile file) {
        try {
            String folder = "C:/CampusLink_New/backend-spring/uploads/chat/";
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path savePath = Paths.get(folder + fileName);
            Files.createDirectories(savePath.getParent());
            file.transferTo(savePath.toFile());

            return "/uploads/chat/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage());
        }
    }

    // =============================
    // 내가 참여한 모든 채팅방 가져오기
    // =============================
    public List<ChatRoomResponse> getMyChatRooms(Integer meId) {

        List<ChatRoom> rooms = chatRooms.findAllRoomsByParticipant(meId);

        return rooms.stream()
                .map(room -> convertRoom(room, meId))
                .toList();
    }

    // =============================
    // ChatRoom → ChatRoomResponse 변환
    // =============================
    private ChatRoomResponse convertRoom(ChatRoom room, Integer meId) {

        ChatRoomResponse dto = new ChatRoomResponse(room.getId());

        dto.setOtherUserName(getOtherUserName(room, meId));
        dto.setLastMessage(getLastMessage(room.getId()));
        dto.setLastMessageTime(getLastMessageTime(room.getId()));

        return dto;
    }

    // 상대방 이름 찾기
    private String getOtherUserName(ChatRoom room, Integer meId) {
        Integer otherId = null;

        // 1:1 채팅
        if (room.getUser1Id() != null && room.getUser2Id() != null) {
            otherId = room.getUser1Id().equals(meId) ? room.getUser2Id() : room.getUser1Id();
        }

        // 물품 기반 채팅
        if (otherId == null) {
            if (room.getLenderId() != null && room.getLenderId().equals(meId))
                otherId = room.getRenterId();
            else
                otherId = room.getLenderId();
        }

        if (otherId == null) return null;

        return users.findById(otherId)
                .map(User::getName)
                .orElse(null);
    }

    // 마지막 메시지 가져오기
    private String getLastMessage(Integer chatId) {
        ChatMessage last = messages.findTop1ByChatIdOrderBySentAtDesc(chatId);
        if (last == null) return null;

        if (last.getMessageType() == MessageType.IMAGE) return "[사진]";
        return last.getContent();
    }
    private LocalDateTime getLastMessageTime(Integer chatId) {
        ChatMessage last = messages.findTop1ByChatIdOrderBySentAtDesc(chatId);
        return last != null ? last.getSentAt() : null;
    }
    // =============================
    // DTO 변환
    // =============================
    private ChatMessageResponse toMessageResponse(ChatMessage m) {
        return ChatMessageResponse.builder()
                .messageId(m.getId())
                .chatId(m.getChatId())
                .senderId(m.getSenderId())
                .messageType(m.getMessageType())
                .content(m.getContent())
                .sentAt(m.getSentAt())
                .build();
    }
}
