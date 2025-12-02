package com.example.campuslink_android.core.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = TokenStore.getToken()
        android.util.Log.d("AuthInterceptor", "토큰 확인 → $token")
        // 토큰 없으면 그대로 진행
        if (token.isNullOrBlank()) {
            android.util.Log.d("AuthInterceptor", "토큰 없음 → 헤더 추가 안함")
            return chain.proceed(originalRequest)
        }

        // 토큰 있으면 Authorization 헤더 추가
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        android.util.Log.d("AuthInterceptor", "Authorization 헤더 추가됨")
        return chain.proceed(newRequest)
    }
}
