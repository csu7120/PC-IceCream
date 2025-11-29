package com.example.campuslink_android.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.UserApi
import com.example.campuslink_android.data.repository.UserRepositoryImpl
import com.example.campuslink_android.domain.model.User
import com.example.campuslink_android.domain.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadMyInfo() {
        viewModelScope.launch {
            try {
                val result = userRepository.getMyInfo()
                _user.value = result
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "내 정보 조회 실패"
            }
        }
    }

    companion object {

        fun create(): ProfileViewModel {
            val api = ApiClient.create(UserApi::class.java)
            val repo: UserRepository = UserRepositoryImpl(api, TokenStore)
            return ProfileViewModel(repo)
        }
    }
}
