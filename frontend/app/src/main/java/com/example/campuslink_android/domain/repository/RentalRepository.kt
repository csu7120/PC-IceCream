package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.data.dto.RentalResponseDto

interface RentalRepository {
    suspend fun requestRental(itemId: Int)
    suspend fun acceptRental(rentalId: Int)

    suspend fun getRequestedRentals(): List<RentalResponseDto>
}
