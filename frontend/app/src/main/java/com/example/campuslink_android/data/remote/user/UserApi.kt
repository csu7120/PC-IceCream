package com.example.campuslink_android.data.remote.user

import com.example.campuslink_android.core.network.ApiResponse
import com.example.campuslink_android.data.remote.auth.LoginRequest
import com.example.campuslink_android.data.remote.auth.LoginResponse
import com.example.campuslink_android.data.remote.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    // 로그인 요청
    @POST("auth/login")
    suspend fun login(
        @Body req: LoginRequest
    ): Response<ApiResponse<LoginResponse>>

    // 내 정보 조회 (토큰은 AuthInterceptor에서 자동 추가)
    @GET("users/me")
    suspend fun getMyInfo(): Response<ApiResponse<UserResponse>>
}
