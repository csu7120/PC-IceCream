package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.data.dto.RentalResponseDto

interface RentalRepository {
    suspend fun requestRental(itemId: Int)
    suspend fun getRequestedRentals(): List<RentalResponseDto> // 내가 빌려준 목록
    suspend fun acceptRental(rentalId: Int)

    // ⭐️ [추가된 부분] 내가 빌린 목록 조회 함수
    suspend fun getMyRentals(): List<RentalResponseDto>
    suspend fun pickupRental(rentalId: Int)
    suspend fun returnRental(rentalId: Int)
}