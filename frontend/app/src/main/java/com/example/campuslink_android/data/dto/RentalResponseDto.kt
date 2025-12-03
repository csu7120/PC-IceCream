package com.example.campuslink_android.data.dto

data class RentalResponseDto(
    val rentalId: Int,
    val itemId: Int,
    val lenderId: Int,
    val renterId: Int,
    val startAt: String?,
    val endAt: String?,
    val dailyPrice: Double?,
    val deposit: Double?,
    val status: String?,
    val pickedUpAt: String?,
    val returnedAt: String?,
    val lateFee: Double?,
    val createdAt: String?,

    // ⭐ 백엔드에서 추가된 필드들
    val itemTitle: String?,
    val itemImageUrl: String?,
    val itemOriginalPrice: Double?,

    // ⭐ 누가 올린 물건인지 표시하기 위한 필드
    val ownerName: String?
)
