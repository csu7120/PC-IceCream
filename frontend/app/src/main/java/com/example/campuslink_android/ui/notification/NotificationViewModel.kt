package com.example.campuslink_android.ui.notification

import androidx.lifecycle.*
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.NotificationApi
import com.example.campuslink_android.data.repository.NotificationRepositoryImpl
import com.example.campuslink_android.data.dto.NotificationResponseDto
import com.example.campuslink_android.domain.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repo: NotificationRepository
) : ViewModel() {

    private val _notifications = MutableLiveData<List<NotificationResponseDto>>()
    val notifications: LiveData<List<NotificationResponseDto>> = _notifications

    private val _unread = MutableLiveData<Int>()
    val unread: LiveData<Int> = _unread

    fun loadNotifications() = viewModelScope.launch {
        _notifications.value = repo.getNotifications()
    }

    fun loadUnreadCount() = viewModelScope.launch {
        _unread.value = repo.getUnreadCount()
    }

    fun markAsRead(id: Long) = viewModelScope.launch {
        repo.markAsRead(id)
        loadNotifications()
        loadUnreadCount()
    }

    companion object {
        fun create(): NotificationViewModel {
            val api = ApiClient.create(NotificationApi::class.java)
            val repo = NotificationRepositoryImpl(api, TokenStore)
            return NotificationViewModel(repo)
        }
    }
}
