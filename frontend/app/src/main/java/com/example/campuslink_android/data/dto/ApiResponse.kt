package com.example.campuslink_android.data.dto

data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?
)
