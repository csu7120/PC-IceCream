package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.data.dto.RentalResponseDto

interface ReviewRepository {
    suspend fun createReview(
        rental: RentalResponseDto,
        rating: Double,
        comment: String,
        roleType: String
    )
}
