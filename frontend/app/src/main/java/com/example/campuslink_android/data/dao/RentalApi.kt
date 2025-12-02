package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.RentalRequestDto
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.core.network.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface RentalApi {

    // 대여 요청
    @POST("/api/rentals")
    suspend fun requestRental(
        @Query("email") email: String,
        @Body body: RentalRequestDto
    ): Response<ApiResponse<RentalResponseDto>>

    // 대여 수락(빌려주는 사람)
    @POST("/api/rentals/{id}/accept")
    suspend fun acceptRental(
        @Path("id") id: Int,
        @Query("email") email: String         // 🔥 lenderEmail → email 로 변경
    ): Response<ApiResponse<RentalResponseDto>>

    // 내가 빌려준 목록 (lender)
    @GET("/api/rentals/me/lent")
    suspend fun getMyLendings(
        @Query("email") email: String
    ): Response<ApiResponse<List<RentalResponseDto>>>

    // 내가 빌린 목록 (renter)
    @GET("/api/rentals/me/borrowed")
    suspend fun getMyRentals(
        @Query("email") email: String
    ): Response<ApiResponse<List<RentalResponseDto>>>
}
