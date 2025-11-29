package com.example.campuslink_android.data.mapper

import com.example.campuslink_android.data.dto.UserDto
import com.example.campuslink_android.domain.model.User

fun UserDto.toDomain(): User =
    User(
        id = userId,
        email = email,
        name = name
    )
