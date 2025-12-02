package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    // 🔹 로그인 (기존 유지)
    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto

    // 🔹 회원가입 API
    @POST("/api/auth/signup")
    fun signup(
        @Body request: SignupRequest
    ): Call<SignupResponse>

    // 🔹 이메일 중복 확인 API
    @GET("/api/users/check-email")
    fun checkEmail(
        @Query("email") email: String
    ): Call<EmailCheckResponse>
}
