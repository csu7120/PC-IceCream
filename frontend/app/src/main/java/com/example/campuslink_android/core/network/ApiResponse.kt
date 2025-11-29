package com.example.campuslink_android.core.network

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)
