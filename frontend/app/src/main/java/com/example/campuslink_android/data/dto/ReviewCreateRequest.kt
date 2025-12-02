package com.example.campuslink_android.data.dto

data class ReviewCreateRequest(
    val rentId: Int,
    val targetUserId: Int,
    val roleType: String,
    val rating: Int,
    val comment: String,
    val tags: List<String>? = null
)
