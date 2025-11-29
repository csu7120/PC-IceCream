package com.example.campuslink_android.data.mapper

import com.example.campuslink_android.data.dto.ItemDto
import com.example.campuslink_android.domain.model.Item

fun ItemDto.toDomain(): Item =
    Item(
        id = itemId,
        title = title,
        category = category,
        price = price,
        ownerName = ownerName,
        thumbnailUrl = thumbnailUrl,
        status = status
    )
