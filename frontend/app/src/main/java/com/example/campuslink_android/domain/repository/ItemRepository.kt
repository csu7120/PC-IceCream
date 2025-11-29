package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.domain.model.Item

interface ItemRepository {
    suspend fun getItems(): List<Item>
}
