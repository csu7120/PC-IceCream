package com.example.campuslink_android.data.dao

import com.example.campuslink_android.core.network.NetworkResponse
import com.example.campuslink_android.data.dto.NotificationResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationApi {

    // ⭐ 전체 알림 조회
    @GET("/api/notifications")
    suspend fun getNotifications(
        @Header("Authorization") token: String
    ): Response<NetworkResponse<List<NotificationResponseDto>>>

    // ⭐ 읽지 않은 알림 개수 조회
    @GET("/api/notifications/unread-count")
    suspend fun getUnreadCount(
        @Header("Authorization") token: String
    ): Response<NetworkResponse<Int>>

    // ⭐ 알림 읽음 처리
    @PATCH("/api/notifications/{id}/read")
    suspend fun markAsRead(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<NetworkResponse<Void>>
}
