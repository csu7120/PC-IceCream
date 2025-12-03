package com.example.campuslink_android.data.dto
import com.google.gson.annotations.SerializedName
data class ItemDto(
    val itemId: Int,
    val title: String,
    val category: String,
    val price: Double,
    val thumbnailUrl: String?,
    @SerializedName("ownerId")
    val ownerId: Int,
    val ownerName: String,
    val status: String?,
    val description: String?
)
