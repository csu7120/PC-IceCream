package com.campuslink.backend.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 일반 1:1 채팅용
    private Integer user1Id;
    private Integer user2Id;

    // 물품 기반 채팅용
    private Integer itemId;
    private Integer lenderId;
    private Integer renterId;

    private LocalDateTime createdAt;
}
