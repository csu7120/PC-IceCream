package com.example.campuslink_android.data.dto

data class SendMessageRequest(
    val messageType: String = "TEXT",
    val content: String
)