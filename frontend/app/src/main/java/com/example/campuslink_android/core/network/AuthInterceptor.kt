package com.example.campuslink_android.core.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = TokenStore.getToken()

        // 토큰 없으면 그대로 진행
        if (token.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        // 토큰 있으면 Authorization 헤더 추가
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}
