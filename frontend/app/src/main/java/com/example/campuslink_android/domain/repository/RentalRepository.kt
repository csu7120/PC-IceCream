package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.data.dto.RentalResponseDto

interface RentalRepository {

    suspend fun getMyRentals(): List<RentalResponseDto>

    suspend fun getMyLendings(): List<RentalResponseDto>

    suspend fun getRequestedRentals(): List<RentalResponseDto>

    suspend fun acceptRental(rentalId: Int)

    suspend fun requestRental(itemId: Int)   // ★ 추가됨
}
