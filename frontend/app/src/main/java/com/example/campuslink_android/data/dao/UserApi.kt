package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.ApiResponse
import com.example.campuslink_android.data.dto.UserInfoResponseDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("/api/users/{id}")
    suspend fun getMyInfo(
        @Path("id") userId: Int
    ): UserInfoResponseDto

    @DELETE("/api/users/me")
    suspend fun deleteUser(
        @Query("email") email: String
    ): ApiResponse<String>
}
