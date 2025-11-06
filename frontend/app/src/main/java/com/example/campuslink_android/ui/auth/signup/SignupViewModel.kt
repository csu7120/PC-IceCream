package com.example.campuslink_android.ui.auth.signup

import androidx.lifecycle.ViewModel
import com.example.campuslink_android.data.remote.auth.SignupRequest
import com.example.campuslink_android.data.remote.auth.SignupResponse
import com.example.campuslink_android.data.repository.UserRepository

class SignupViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    suspend fun signup(request: SignupRequest): SignupResponse? {
        return try {
            val res = repository.signup(request)
            res.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
