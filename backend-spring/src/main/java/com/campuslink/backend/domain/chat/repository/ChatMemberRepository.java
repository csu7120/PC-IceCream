package com.campuslink.backend.domain.chat.repository;

import com.campuslink.backend.domain.chat.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Integer> {

    List<ChatMember> findByChat_Id(Integer chatId);

    Optional<ChatMember> findByChat_IdAndUser_UserId(Integer chatId, Integer userId);
}
