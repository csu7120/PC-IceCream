package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.data.dto.ChatMessage
import com.example.campuslink_android.data.dto.ChatRoomResponseDto
import okhttp3.MultipartBody

interface ChatRepository {

    suspend fun openDirectChat(targetUserId: Int): Int

    suspend fun loadMessages(roomId: Int): List<ChatMessage>

    suspend fun sendTextMessage(roomId: Int, message: String)

    suspend fun sendImageMessage(roomId: Int, imageUrl: String)

    suspend fun uploadImage(filePart: MultipartBody.Part): String

    suspend fun getChatRooms(): List<ChatRoomResponseDto>
}
