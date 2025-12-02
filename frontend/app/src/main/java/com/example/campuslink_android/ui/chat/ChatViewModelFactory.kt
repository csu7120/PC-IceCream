package com.example.campuslink_android.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.ChatApi
import com.example.campuslink_android.data.repository.ChatRepositoryImpl

class ChatViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {

            val repository = ChatRepositoryImpl(
                api = ApiClient.create(ChatApi::class.java),
                tokenStore = TokenStore
            )

            return ChatViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
