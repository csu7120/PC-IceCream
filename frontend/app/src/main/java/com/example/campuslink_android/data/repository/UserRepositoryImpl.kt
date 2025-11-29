package com.example.campuslink_android.data.repository

import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.UserApi
import com.example.campuslink_android.data.mapper.toDomain
import com.example.campuslink_android.domain.model.User
import com.example.campuslink_android.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val tokenStore: TokenStore
) : UserRepository {

    override suspend fun getMyInfo(): User {
        val userId = tokenStore.getUserId()
            ?: throw IllegalStateException("로그인된 사용자 ID가 없습니다.")

        val response = userApi.getMyInfo(userId)

        if (!response.success) {
            throw IllegalStateException(response.message ?: "내 정보 조회 실패")
        }

        val dto = response.data
            ?: throw IllegalStateException("서버에서 사용자 데이터를 받지 못했습니다.")

        return dto.toDomain()
    }
}
