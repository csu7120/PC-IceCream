package com.example.campuslink_android.data.dto

data class UserInfoResponseDto(
    val success: Boolean,
    val data: UserDto?,
    val message: String?
)
