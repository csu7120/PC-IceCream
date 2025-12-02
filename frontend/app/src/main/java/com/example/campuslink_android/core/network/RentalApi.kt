package com.example.campuslink_android.core.network

import com.example.campuslink_android.data.dto.RentalRequestDto
import com.example.campuslink_android.data.dto.RentalResponseDto
import retrofit2.Response
import retrofit2.http.*

interface RentalApi {
    /**
     * 1. 대여 요청 보내기
     *
     * POST /api/rentals?email={email}
     */
    @POST("/api/rentals")
    suspend fun requestRental(
        @Query("email") email: String,
        @Body body: RentalRequestDto
    ): Response<NetworkResponse<RentalResponseDto>>
    /**
     * 2. 대여 요청 수락하기
     *
     * POST /api/rentals/{id}/accept?lenderEmail=...
     */
    @POST("/api/rentals/{id}/accept")
    suspend fun acceptRental(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<NetworkResponse<RentalResponseDto>>

    /**
     * 3. 내가 빌려준 목록 (LENDER)
     *
     * GET /api/rentals/lendings/me?lenderEmail=...
     */
    @GET("/api/rentals/lendings/me")
    suspend fun getMyLendings(
        @Query("lenderEmail") lenderEmail: String
    ): Response<NetworkResponse<List<RentalResponseDto>>>
    /**
     * 4. 내가 빌린 목록 (RENTER)
     *
     * GET /api/rentals/borrowings/me?renterEmail=...
     */
    @GET("/api/rentals/borrowings/me")
    suspend fun getMyRentals(
        @Query("renterEmail") renterEmail: String
    ): Response<NetworkResponse<List<RentalResponseDto>>>
}
