package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.RentalRequestDto
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.core.network.NetworkResponse
import retrofit2.Response
import retrofit2.http.*

interface RentalApi {

    // ⭐ 대여 요청
    @POST("/api/rentals")
    suspend fun requestRental(
        @Query("email") email: String,
        @Body body: RentalRequestDto
    ): Response<NetworkResponse<RentalResponseDto>>

    // ⭐ 대여 수락 (JWT 인증 방식)
    @POST("/api/rentals/{id}/accept")
    suspend fun acceptRental(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<NetworkResponse<RentalResponseDto>>

    // ⭐ 내가 빌려준 목록
    @GET("/api/rentals/lendings/me")
    suspend fun getMyLendings(
        @Query("lenderEmail") lenderEmail: String
    ): Response<NetworkResponse<List<RentalResponseDto>>>

    // ⭐ 내가 빌린 목록
    @GET("/api/rentals/borrowings/me")
    suspend fun getMyRentals(
        @Query("renterEmail") renterEmail: String
    ): Response<NetworkResponse<List<RentalResponseDto>>>
}
