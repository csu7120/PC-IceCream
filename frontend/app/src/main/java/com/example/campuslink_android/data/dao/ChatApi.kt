package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.ChatRoomResponseDto
import com.example.campuslink_android.data.dto.ApiResponse
import com.example.campuslink_android.data.dto.ChatMessage
import com.example.campuslink_android.data.dto.SendMessageRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ChatApi {

    @POST("/api/chats/direct/{targetUserId}")
    suspend fun openDirectChat(
        @Path("targetUserId") targetUserId: Int
    ): ApiResponse<ChatRoomResponseDto>

    @GET("/api/chats/{chatId}/messages")
    suspend fun getMessages(
        @Path("chatId") chatId: Int
    ): ApiResponse<List<ChatMessage>>

    @POST("/api/chats/{chatId}/messages")
    suspend fun sendMessage(
        @Path("chatId") chatId: Int,
        @Body request: SendMessageRequest
    ): ApiResponse<ChatMessage>
    @Multipart
    @POST("/api/chats/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): ApiResponse<String>
    @GET("api/chats/rooms")
    suspend fun getMyChatRooms(): ApiResponse<List<ChatRoomResponseDto>>
}


