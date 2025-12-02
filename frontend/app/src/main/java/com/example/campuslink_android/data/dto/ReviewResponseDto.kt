package com.example.campuslink_android.data.dto

data class ReviewResponseDto(
    val reviewId: Int,
    val reviewerId: Int,
    val revieweeId: Int,
    val rentalId: Int,
    val rating: Double,
    val comment: String,
    val createdAt: String
)
