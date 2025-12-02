package com.example.campuslink_android.data.mapper

import com.example.campuslink_android.data.dto.ItemDto
import com.example.campuslink_android.data.dto.ItemResponseDto
import com.example.campuslink_android.domain.model.Item

// 이미 있는 ItemDto → Item
fun ItemDto.toDomain(): Item =
    Item(
        id = itemId,
        title = title,
        category = category,
        price = price,
        ownerId = ownerId,       // ★ 반드시 필요
        ownerName = ownerName,
        thumbnailUrl = thumbnailUrl,
        status = status
    )

// ★ 새로 추가해야 하는 ItemResponseDto → Item
fun ItemResponseDto.toDomain(): Item =
    Item(
        id = itemId,
        title = title,
        category = category,
        price = price.toDouble(),
        ownerId = ownerId,             // ← 반드시 추가
        ownerName = ownerName ?: "",                // 서버가 ownerName 안 주는 경우 빈 값 처리
        thumbnailUrl = images?.firstOrNull(),
        status = null
    )
