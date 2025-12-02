package com.campuslink.backend.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer chatId;

    private Integer senderId;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;   // TEXT, IMAGE, LOCATION

    // ------ TEXT 메시지 ------
    @Column(length = 1000)
    private String content;

    // ------ IMAGE 메시지 ------
    @Column(length = 500)
    private String imageUrl;

    // ------ LOCATION 메시지 ------
    private Double latitude;
    private Double longitude;

    @Column(length = 255)
    private String locationLabel;

    private LocalDateTime sentAt;
}
