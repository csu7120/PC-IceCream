package com.campuslink.backend.domain.notification.repository;

import com.campuslink.backend.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 내 알림 리스트
    List<Notification> findByUserUserIdOrderByCreatedAtDesc(Integer userId);

    // 안 읽은 알림 개수
    long countByUserUserIdAndIsReadFalse(Integer userId);
}
