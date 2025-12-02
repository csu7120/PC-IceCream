package com.campuslink.backend.domain.chat.repository;

import com.campuslink.backend.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    List<ChatMessage> findByChatIdOrderBySentAtAsc(Integer chatId);

    // 최근 메시지 1개 가져오기 위해 추가하는 게 좋음
    ChatMessage findTop1ByChatIdOrderBySentAtDesc(Integer chatId);
    
    
}

