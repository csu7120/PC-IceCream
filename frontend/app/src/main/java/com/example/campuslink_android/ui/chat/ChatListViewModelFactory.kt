package com.example.campuslink_android.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.campuslink_android.domain.repository.ChatRepository

class ChatListViewModelFactory(
    private val repository: ChatRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatListViewModel::class.java)) {
            return ChatListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
