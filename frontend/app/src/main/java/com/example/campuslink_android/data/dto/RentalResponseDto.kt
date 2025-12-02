package com.example.campuslink_android.data.dto

data class RentalResponseDto(
    val rentalId: Int,
    val itemId: Int,
    val lenderId: Int,
    val renterId: Int,   // 🔥 borrowerId → renterId 로 수정
    val startAt: String,
    val lenderEmail: String?,
    val endAt: String,
    val status: String,
    val price: Int
)
