package com.example.campuslink_android.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.domain.repository.ChatRepository
import com.example.campuslink_android.data.dto.ChatRoomResponseDto
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    val chatRooms = MutableLiveData<List<ChatRoomResponseDto>>()

    fun loadChatRooms() {
        viewModelScope.launch {
            try {
                val rooms = repository.getChatRooms()
                chatRooms.postValue(rooms)
            } catch (e: Exception) {
                chatRooms.postValue(emptyList())  // 오류 시 빈 리스트
            }
        }
    }
}
