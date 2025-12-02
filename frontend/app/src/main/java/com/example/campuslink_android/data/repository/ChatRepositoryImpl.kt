package com.example.campuslink_android.data.repository

import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.ChatApi
import com.example.campuslink_android.data.dto.ChatMessage
import com.example.campuslink_android.data.dto.ChatRoomResponseDto
import com.example.campuslink_android.data.dto.SendMessageRequest
import com.example.campuslink_android.domain.repository.ChatRepository
import okhttp3.MultipartBody

class ChatRepositoryImpl(
    private val api: ChatApi,
    private val tokenStore: TokenStore
) : ChatRepository {

    override suspend fun openDirectChat(targetUserId: Int): Int {
        val response = api.openDirectChat(targetUserId)
        return response.data?.chatId ?: throw Exception("chatId 없음")
    }

    override suspend fun loadMessages(roomId: Int): List<ChatMessage> {
        return api.getMessages(roomId).data ?: emptyList()
    }

    override suspend fun sendTextMessage(roomId: Int, message: String) {
        api.sendMessage(
            roomId,
            SendMessageRequest(
                messageType = "TEXT",
                content = message
            )
        )
    }

    override suspend fun sendImageMessage(roomId: Int, imageUrl: String) {
        api.sendMessage(
            roomId,
            SendMessageRequest(
                messageType = "IMAGE",
                content = imageUrl
            )
        )
    }

    override suspend fun uploadImage(filePart: MultipartBody.Part): String {
        val response = api.uploadImage(filePart)
        return response.data ?: throw Exception("이미지 URL 없음")
    }

    override suspend fun getChatRooms(): List<ChatRoomResponseDto> {
        val response = api.getMyChatRooms()
        return response.data ?: emptyList()
    }
}
