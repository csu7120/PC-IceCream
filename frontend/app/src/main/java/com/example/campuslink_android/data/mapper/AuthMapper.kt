package com.example.campuslink_android.data.mapper

import com.example.campuslink_android.data.dto.LoginResponseDto
import com.example.campuslink_android.domain.model.User

fun LoginResponseDto.toDomainUser(): User =
    User(
        id = userId,
        email = email,
        name = name
    )
