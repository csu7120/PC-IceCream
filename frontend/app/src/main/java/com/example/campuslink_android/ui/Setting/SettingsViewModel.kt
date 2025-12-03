package com.example.campuslink_android.ui.Setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.domain.repository.UserRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _deleteResult = MutableLiveData<String>()
    val deleteResult: LiveData<String> = _deleteResult

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                val msg = userRepository.deleteUser()
                _deleteResult.postValue(msg)
            } catch (e: Exception) {
                _deleteResult.postValue("탈퇴 실패: ${e.message}")
            }
        }
    }
}
