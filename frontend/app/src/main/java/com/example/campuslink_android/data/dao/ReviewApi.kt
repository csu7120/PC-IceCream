package com.example.campuslink_android.data.dao

import com.example.campuslink_android.core.network.NetworkResponse
import com.example.campuslink_android.data.dto.ReviewCreateRequest
import com.example.campuslink_android.data.dto.ReviewResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ReviewApi {

    @POST("/api/reviews")
    suspend fun createReview(
        @Header("Authorization") token: String,
        @Body body: ReviewCreateRequest
    ): Response<NetworkResponse<ReviewResponseDto>>
}
