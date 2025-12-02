package com.example.campuslink_android.domain.model

data class Item(
    val id: Int,
    val title: String,
    val description: String?,
    val price: Double,
    val category: String,
    val images: List<String>,
    val ownerId: Int?,
    val ownerName: String?,      // ★ 추가
    val createdAt: String?,
    val status: String?
)
