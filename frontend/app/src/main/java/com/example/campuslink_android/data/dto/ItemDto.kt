package com.example.campuslink_android.data.dto

data class ItemDto(
    val itemId: Int,
    val title: String,
    val category: String,
    val price: Double,
    val thumbnailUrl: String?,
    val ownerName: String,
    val status: String?
)
