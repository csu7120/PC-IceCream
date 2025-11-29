package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.UserInfoResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("/api/users/{id}")
    suspend fun getMyInfo(
        @Path("id") userId: Int
    ): UserInfoResponseDto
}
