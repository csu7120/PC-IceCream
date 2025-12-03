package com.example.campuslink_android.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.campuslink_android.databinding.ItemChatRoomBinding
import com.example.campuslink_android.data.dto.ChatRoomResponseDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

class ChatListAdapter(
    private val onClick: (ChatRoomResponseDto) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.ChatRoomViewHolder>() {

    private val items = mutableListOf<ChatRoomResponseDto>()

    fun submitList(newList: List<ChatRoomResponseDto>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ChatRoomViewHolder(private val binding: ItemChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatRoomResponseDto) {
            binding.tvUserName.text = item.otherUserName
            binding.tvLastMessage.text = item.lastMessage ?: ""

            // ⭐ 시간 포맷 적용
            binding.tvTime.text = formatRelativeTime(item.lastMessageTime)

            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val binding = ItemChatRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size


    // ⭐ 상대시간 변환 함수
    private fun formatRelativeTime(timeString: String?): String {
        if (timeString.isNullOrEmpty()) return ""

        return try {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val time = LocalDateTime.parse(timeString, formatter)

            val now = LocalDateTime.now()
            val duration = Duration.between(time, now)

            when {
                duration.toMinutes() < 1 -> "방금 전"
                duration.toMinutes() < 60 -> "${duration.toMinutes()}분 전"
                duration.toHours() < 24 -> "${duration.toHours()}시간 전"
                duration.toDays() == 1L -> "어제"
                duration.toDays() < 7 -> "${duration.toDays()}일 전"
                else -> time.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            }
        } catch (e: Exception) {
            timeString
        }
    }

}


