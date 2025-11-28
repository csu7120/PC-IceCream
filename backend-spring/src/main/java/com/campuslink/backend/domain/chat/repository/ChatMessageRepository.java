package com.campuslink.backend.domain.chat.repository;

import com.campuslink.backend.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    List<ChatMessage> findByChat_IdOrderBySentAtAsc(Integer chatId);

    long countByChat_IdAndSentAtAfterAndSender_UserIdNot(
            Integer chatId,
            LocalDateTime after,
            Integer notSenderId
    );
}
