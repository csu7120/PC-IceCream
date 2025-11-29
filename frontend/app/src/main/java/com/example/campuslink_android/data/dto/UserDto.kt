package com.example.campuslink_android.data.dto

data class UserDto(
    val userId: Int,
    val email: String?,
    val name: String?,
    val profileUrl: String?,
    val isVerified: Boolean?
)
