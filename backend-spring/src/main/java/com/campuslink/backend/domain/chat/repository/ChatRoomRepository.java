package com.campuslink.backend.domain.chat.repository;

import com.campuslink.backend.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

    // 내가 속한 방 목록
    @Query("""
           SELECT DISTINCT c
           FROM ChatRoom c
           JOIN c.members m
           WHERE m.user.userId = :userId
           ORDER BY c.id DESC
           """)
    List<ChatRoom> findAllByMember(@Param("userId") Integer userId);

    // user1, user2가 둘 다 멤버인 방 (1:1 기준)
    @Query("""
           SELECT c
           FROM ChatRoom c
             JOIN c.members m1
             JOIN c.members m2
           WHERE m1.user.userId = :user1
             AND m2.user.userId = :user2
           """)
    Optional<ChatRoom> findDirectChatBetween(
            @Param("user1") Integer user1,
            @Param("user2") Integer user2
    );
}
