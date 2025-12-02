package com.example.campuslink_android.data.dto

import com.google.gson.annotations.SerializedName

data class ChatRoomResponseDto(
    @SerializedName("chatId")
    val chatId: Int,
    val roomId: Int,
    val otherUserName: String,
    val lastMessage: String?,
    val lastMessageTime: String?
)
