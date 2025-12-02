package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.data.dto.NotificationResponseDto

interface NotificationRepository {

    suspend fun getNotifications(): List<NotificationResponseDto>

    suspend fun getUnreadCount(): Int

    suspend fun markAsRead(id: Long)
}
