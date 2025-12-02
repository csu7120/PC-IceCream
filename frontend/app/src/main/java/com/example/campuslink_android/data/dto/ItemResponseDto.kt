package com.example.campuslink_android.data.dto

data class ItemResponseDto(
    val itemId: Int,
    val ownerId: Int,
    val title: String,
    val description: String?,
    val price: Int,
    val ownerName: String?,
    val category: String,
    val images: List<String>? = emptyList()
)
