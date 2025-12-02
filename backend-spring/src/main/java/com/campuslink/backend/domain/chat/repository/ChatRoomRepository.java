package com.campuslink.backend.domain.chat.repository;

import com.campuslink.backend.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

    // 일반 1:1 채팅 (정렬없이 찾기만)
    Optional<ChatRoom> findByUser1IdAndUser2Id(Integer user1Id, Integer user2Id);
    Optional<ChatRoom> findByUser2IdAndUser1Id(Integer user2Id, Integer user1Id);

    @Query("""
            SELECT r FROM ChatRoom r
            WHERE r.user1Id = :userId
               OR r.user2Id = :userId
               OR r.lenderId = :userId
               OR r.renterId = :userId
            ORDER BY r.createdAt DESC
        """)
        List<ChatRoom> findAllRoomsByParticipant(@Param("userId") Integer userId);
}
