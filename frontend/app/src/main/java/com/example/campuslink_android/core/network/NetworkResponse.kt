package com.example.campuslink_android.core.network

data class NetworkResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)
