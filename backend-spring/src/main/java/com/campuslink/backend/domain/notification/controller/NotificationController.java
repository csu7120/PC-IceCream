package com.campuslink.backend.domain.notification.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.notification.dto.NotificationRequest;
import com.campuslink.backend.domain.notification.dto.NotificationResponse;
import com.campuslink.backend.domain.notification.service.NotificationService;
import com.campuslink.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 내 알림 전체 조회
     */
    @GetMapping
    public ApiResponse<List<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<NotificationResponse> notifications =
                notificationService.getMyNotifications(user.getUserId());
        return ApiResponse.ok(notifications);
    }

    /**
     * 안 읽은 알림 개수 조회 (홈 상단 뱃지 등)
     */
    @GetMapping("/unread-count")
    public ApiResponse<Long> getUnreadCount(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        long count = notificationService.getUnreadCount(user.getUserId());
        return ApiResponse.ok(count);
    }

    /**
     * 알림 읽음 처리
     */
    @PatchMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(
            @PathVariable("id") Long notificationId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        notificationService.markAsRead(notificationId, user.getUserId());
        return ApiResponse.ok(null);
    }

    /**
     * (선택) 관리자/테스트용: 임의 알림 생성
     * 실제 서비스에서 안 쓰면 이 메서드는 빼도 됨
     */
    @PostMapping
    public ApiResponse<Void> createNotification(
            @RequestBody NotificationRequest request
    ) {
        notificationService.notifyUser(request.getUserId(), request.getType(), request.getMessage());
        return ApiResponse.ok(null);
    }
}
