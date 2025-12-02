package com.example.campuslink_android.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.campuslink_android.databinding.ItemChatRoomBinding
import com.example.campuslink_android.data.dto.ChatRoomResponseDto
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
}
