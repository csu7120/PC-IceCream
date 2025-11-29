package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.RentalRequestDto
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.core.network.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface RentalApi {

    @POST("/api/rentals")
    suspend fun requestRental(
        @Query("email") email: String,
        @Body body: RentalRequestDto
    ): Response<ApiResponse<RentalResponseDto>>

    @POST("/api/rentals/{id}/accept")
    suspend fun acceptRental(
        @Path("id") id: Int,
        @Query("lenderEmail") lenderEmail: String
    ): Response<ApiResponse<RentalResponseDto>>

    @GET("/api/rentals/lendings/me")
    suspend fun getMyLendings(
        @Query("lenderEmail") lenderEmail: String
    ): Response<ApiResponse<List<RentalResponseDto>>>

    // ⭐️ [추가된 부분] 내가 빌린 목록 조회 API
    @GET("/api/rentals/rentals/me")
    suspend fun getMyRentals(
        @Query("renterEmail") renterEmail: String
    ): Response<ApiResponse<List<RentalResponseDto>>>
}