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
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRooms;
    private final ChatMessageRepository messages;
    private final UserRepository users;

    // 유저 검증
    private User getUser(Integer id) {
        return users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 없음"));
    }

    // ----------------------------------------------------
    // 1) 일반 프로필 기반 채팅
    // ----------------------------------------------------
    public ChatRoomResponse openDirectChat(Integer meId, Integer targetId) {

        if (meId.equals(targetId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신과는 채팅 불가");
        }

        var existing = chatRooms.findByUser1IdAndUser2Id(meId, targetId)
                .or(() -> chatRooms.findByUser2IdAndUser1Id(meId, targetId))
                .orElse(null);

        if (existing != null) {
            return new ChatRoomResponse(existing.getId());
        }

        ChatRoom room = ChatRoom.builder()
                .user1Id(meId)
                .user2Id(targetId)
                .createdAt(LocalDateTime.now())
                .build();

        room = chatRooms.save(room);

        return new ChatRoomResponse(room.getId());
    }

    // ----------------------------------------------------
    // 2) 물품 기반 채팅
    // ----------------------------------------------------
   

    // ----------------------------------------------------
    // 3) 메시지 조회
    // ----------------------------------------------------
    public List<ChatMessageResponse> getMessages(Integer chatId, Integer meId) {

        ChatRoom room = chatRooms.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "방 없음"));

        // 참여자 검증
        if (!isParticipant(room, meId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "참여자가 아님");

        return messages.findByChatIdOrderBySentAtAsc(chatId)
                .stream()
                .map(this::toMessageResponse)
                .toList();
    }

    // 참여자 체크
    private boolean isParticipant(ChatRoom r, Integer meId) {
        return (r.getUser1Id() != null && r.getUser1Id().equals(meId)) ||
               (r.getUser2Id() != null && r.getUser2Id().equals(meId)) ||
               (r.getLenderId() != null && r.getLenderId().equals(meId)) ||
               (r.getRenterId() != null && r.getRenterId().equals(meId));
    }

    // ----------------------------------------------------
    // 4) 메시지 보내기
    // ----------------------------------------------------
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
    public List<ChatRoomResponse> getMyChatRooms(Integer meId) {
        List<ChatRoom> rooms = chatRooms.findAll(); // 또는 커스텀 쿼리

        return rooms.stream()
                .filter(r ->
                        (r.getUser1Id() != null && r.getUser1Id().equals(meId)) ||
                        (r.getUser2Id() != null && r.getUser2Id().equals(meId)) ||
                        (r.getLenderId() != null && r.getLenderId().equals(meId)) ||
                        (r.getRenterId() != null && r.getRenterId().equals(meId))
                )
                .map(room -> new ChatRoomResponse(room.getId()))
                .toList();
    }
    private String getOtherUserName(ChatRoom room, Integer meId) {
        Integer otherId = room.getUser1Id().equals(meId)
                ? room.getUser2Id()
                : room.getUser1Id();

        return users.findById(otherId)
                .map(User::getName)
                .orElse(null);
    }
    private String getLastMessage(Integer chatId) {
        ChatMessage last = messages.findTop1ByChatIdOrderBySentAtDesc(chatId);
        if (last == null) return null;

        if (last.getMessageType() == MessageType.IMAGE) return "[사진]";

        return last.getContent();
    }
    // ----------------------------------------------------
    // 5) DTO 변환
    // ----------------------------------------------------
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
