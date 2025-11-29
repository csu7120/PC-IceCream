package com.campuslink.backend.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationRequest {

    // 알림을 받을 사용자
    private Integer userId;

    // 알림 타입 (예: RENT_REQUEST, RENT_DUE, CHAT_NEW 등)
    private String type;

    // 실제 알림 내용
    private String message;
}
