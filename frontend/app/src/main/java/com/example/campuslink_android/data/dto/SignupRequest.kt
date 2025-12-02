package com.example.campuslink_android.data.dto

data class SignupRequest(
    val campusId: Int,
    val email: String,
    val password: String,
    val name: String,
    val phone: String
)
