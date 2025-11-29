package com.example.campuslink_android.data.repository

import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.AuthApi
import com.example.campuslink_android.data.dto.LoginRequestDto
import com.example.campuslink_android.data.mapper.toDomainUser
import com.example.campuslink_android.domain.model.User
import com.example.campuslink_android.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) : AuthRepository {

    override suspend fun login(email: String, password: String): User {
        val response = authApi.login(LoginRequestDto(email, password))

        tokenStore.saveLoginInfo(
            token = response.token,
            expiresInMinutes = response.expiresInMinutes,
            userId = response.userId
        )
        tokenStore.saveEmail(response.email)

        return response.toDomainUser()
    }
}
