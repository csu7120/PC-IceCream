package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.domain.model.Item

interface ItemRepository {
    // 전체 물품
    suspend fun getItems(): List<Item>

    // 내가 올린 물품
    suspend fun getMyItems(userId: Int): List<Item>
}
