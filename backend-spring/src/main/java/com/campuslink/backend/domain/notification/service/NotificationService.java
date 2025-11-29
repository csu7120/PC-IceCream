package com.campuslink.backend.domain.notification.service;

import com.campuslink.backend.common.exception.BusinessException;
import com.campuslink.backend.common.exception.ErrorCode;
import com.campuslink.backend.domain.notification.dto.NotificationResponse;
import com.campuslink.backend.domain.notification.entity.Notification;
import com.campuslink.backend.domain.notification.repository.NotificationRepository;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /**
     * 내부 도메인(대여/채팅 등)에서 호출해서 알림 생성하는 메서드
     */
    @Transactional
    public void notifyUser(Integer userId, String type, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Notification notification = Notification.of(user, type, message);
        notificationRepository.save(notification);

        // 나중에 FCM 푸시 붙일 거면 여기서 pushService 호출하면 됨.
        // pushService.sendToUser(user, message);
    }

    /**
     * 내 알림 전체 조회
     */
    public List<NotificationResponse> getMyNotifications(Integer userId) {
        return notificationRepository.findByUserUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    /**
     * 안 읽은 알림 개수 조회 (뱃지 등)
     */
    public long getUnreadCount(Integer userId) {
        return notificationRepository.countByUserUserIdAndIsReadFalse(userId);
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    public void markAsRead(Long notificationId, Integer userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND));

        // 내 알림이 아니면 에러
        if (!notification.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        notification.markAsRead();
    }
}
