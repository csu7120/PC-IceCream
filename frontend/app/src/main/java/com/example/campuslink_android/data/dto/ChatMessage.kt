package com.example.campuslink_android.data.dto

data class ChatMessage(
    val messageId: Int,
    val chatId: Int,
    val senderId: Int,
    val content: String?,
    val messageType: String,
    val sentAt: String
)
