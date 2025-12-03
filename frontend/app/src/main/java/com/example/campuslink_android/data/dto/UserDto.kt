package com.example.campuslink_android.data.dto

data class UserDto(
    val userId: Int,
    val campusId: Int?,
    val email: String?,
    val password: String?,
    val name: String?,
    val phone: String?,
    val profileUrl: String?,
    val isVerified: Boolean?,
    val ratingAvg: Double?,
    val createdAt: String?,
    val updatedAt: String?
)
