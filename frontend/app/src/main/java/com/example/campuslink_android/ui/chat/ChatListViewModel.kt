package com.example.campuslink_android.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.domain.repository.ChatRepository
import com.example.campuslink_android.data.dto.ChatRoomResponseDto
import kotlinx.coroutines.launch
import android.util.Log
class ChatListViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    val chatRooms = MutableLiveData<List<ChatRoomResponseDto>>()

    fun loadChatRooms() {
        viewModelScope.launch {
            try {
                val rooms = repository.getChatRooms()
                Log.d("ChatListViewModel", "📦 rooms from repository = $rooms")
                Log.d("ChatListViewModel", "📦 rooms size = ${rooms.size}")
                chatRooms.postValue(rooms)
            } catch (e: Exception) {
                Log.e("ChatListViewModel", "❌ Failed to load chat rooms", e)
                chatRooms.postValue(emptyList())  // 오류 시 빈 리스트
            }
        }
    }
}
