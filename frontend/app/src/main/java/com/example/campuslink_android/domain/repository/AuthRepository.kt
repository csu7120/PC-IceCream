package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User
}
