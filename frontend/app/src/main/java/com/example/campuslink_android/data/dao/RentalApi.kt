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
}
