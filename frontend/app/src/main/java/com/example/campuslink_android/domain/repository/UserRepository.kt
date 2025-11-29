package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.domain.model.User

interface UserRepository {
    suspend fun getMyInfo(): User
}
