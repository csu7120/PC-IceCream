package com.example.campuslink_android.data.mapper

import com.example.campuslink_android.data.dto.ItemDto
import com.example.campuslink_android.data.dto.ItemResponseDto
import com.example.campuslink_android.domain.model.Item

fun ItemDto.toDomain(): Item {
    return Item(
        id = this.itemId,
        title = this.title,
        description = null,
        price = this.price,
        category = this.category,
        images = listOfNotNull(this.thumbnailUrl),
        ownerId = null,
        ownerName = this.ownerName,   // ★ 추가
        createdAt = null,
        status = this.status
    )
}



fun ItemResponseDto.toDomain(): Item {
    return Item(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        category = this.category,
        images = this.images,
        ownerId = this.ownerId,
        ownerName = null,           // ★ 추가
        createdAt = this.createdAt,
        status = null
    )
}

