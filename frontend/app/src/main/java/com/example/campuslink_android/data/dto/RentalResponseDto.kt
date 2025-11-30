package com.example.campuslink_android.data.dto

data class RentalResponseDto(
    val rentalId: Int,
    val itemId: Int,
    val borrowerId: Int,
    val lenderId: Int,
    val startAt: String,
    val endAt: String,
    val status: String,
    val Price: Int
)
