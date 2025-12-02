package com.example.campuslink_android.data.dto

data class NotificationResponseDto(
    val notificationId: Long,
    val type: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String
)
