package com.example.campuslink_android.domain.model

data class Item(
    val id: Int,
    val title: String,
    val category: String,
    val price: Double,
    val ownerId: Int,
    val ownerName: String,
    val thumbnailUrl: String?,
    val status: String?,

    val description: String? = null,
    val imageUrl: String? = null
)

