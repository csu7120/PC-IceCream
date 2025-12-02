package com.example.campuslink_android.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.data.dto.ChatMessage
import com.example.campuslink_android.domain.repository.ChatRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ChatViewModel(
    private val repo: ChatRepository
) : ViewModel() {

    // 🔥 ItemDetailActivity와 호환되도록 추가
    private val _roomId = MutableLiveData<Int?>()
    val roomId: LiveData<Int?> = _roomId

    fun openChat(targetUserId: Int) {
        viewModelScope.launch {
            try {
                val created = repo.openDirectChat(targetUserId)
                _roomId.value = created
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ------------------------------
    // 메시지 관련
    // ------------------------------
    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages

    fun loadMessages(roomId: Int) {
        viewModelScope.launch {
            _messages.value = repo.loadMessages(roomId)
        }
    }

    fun sendText(roomId: Int, message: String) {
        viewModelScope.launch {
            repo.sendTextMessage(roomId, message)
            loadMessages(roomId)
        }
    }

    fun sendImage(roomId: Int, filePart: MultipartBody.Part) {
        viewModelScope.launch {
            val url = repo.uploadImage(filePart)
            repo.sendImageMessage(roomId, url)
            loadMessages(roomId)
        }
    }
}
