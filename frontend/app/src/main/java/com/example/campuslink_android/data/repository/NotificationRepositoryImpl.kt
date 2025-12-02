package com.example.campuslink_android.data.repository

import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.NotificationApi
import com.example.campuslink_android.data.dto.NotificationResponseDto
import com.example.campuslink_android.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val api: NotificationApi,
    private val tokenStore: TokenStore
) : NotificationRepository {

    override suspend fun getNotifications(): List<NotificationResponseDto> {
        val token = tokenStore.getToken() ?: throw IllegalStateException("로그인 필요")
        val bearer = "Bearer $token"

        val res = api.getNotifications(bearer)
        if (!res.isSuccessful) throw IllegalStateException("알림 조회 실패")
        return res.body()?.data ?: emptyList()
    }

    override suspend fun getUnreadCount(): Int {
        val token = tokenStore.getToken() ?: throw IllegalStateException("로그인 필요")
        val bearer = "Bearer $token"

        val res = api.getUnreadCount(bearer)
        if (!res.isSuccessful) throw IllegalStateException("알림 개수 조회 실패")
        return res.body()?.data ?: 0
    }

    override suspend fun markAsRead(id: Long) {
        val token = tokenStore.getToken() ?: throw IllegalStateException("로그인 필요")
        val bearer = "Bearer $token"

        val res = api.markAsRead(id, bearer)
        if (!res.isSuccessful) throw IllegalStateException("읽음 처리 실패")
    }
}
