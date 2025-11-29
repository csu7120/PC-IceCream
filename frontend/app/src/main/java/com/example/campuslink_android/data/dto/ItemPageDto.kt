package com.example.campuslink_android.data.dto

data class ItemPageDto(
    val content: List<ItemDto>?,
    val page: Int?,
    val size: Int?,
    val totalElements: Long?
)
