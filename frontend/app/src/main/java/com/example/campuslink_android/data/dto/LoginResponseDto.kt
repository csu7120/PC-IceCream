package com.example.campuslink_android.data.dto

data class LoginResponseDto(
    val token: String,
    val expiresInMinutes: Int,
    val userId: Int,
    val email: String,
    val name: String
)
