package com.example.campuslink_android.data.dto

data class ItemResponseDto(
    val id: Int,
    val title: String,
    val description: String?,
    val price: Double,
    val category: String,
    val ownerId: Int,
    val images: List<String> = emptyList(),
    val createdAt: String
)
