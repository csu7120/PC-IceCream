package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.domain.model.Item
import java.io.File

interface ItemRepository {
    // 전체 물품
    suspend fun getItems(): List<Item>

    // 내가 올린 물품
    suspend fun getMyItems(userId: Int): List<Item>

    // ⭐ 물품 등록
    suspend fun registerItem(
        title: String,
        description: String?,
        price: Double,
        category: String,
        userId: Int,
        images: List<File>? = null
    ): Item
}
