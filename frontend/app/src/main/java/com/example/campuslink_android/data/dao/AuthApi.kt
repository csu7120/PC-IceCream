package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.LoginRequestDto
import com.example.campuslink_android.data.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto
}
