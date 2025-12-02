package com.example.campuslink_android.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.domain.repository.NotificationRepository

class NotificationViewModelFactory(
    private val repo: NotificationRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
