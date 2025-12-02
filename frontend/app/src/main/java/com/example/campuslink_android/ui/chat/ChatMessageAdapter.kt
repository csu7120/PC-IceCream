package com.example.campuslink_android.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campuslink_android.R
import com.example.campuslink_android.data.dto.ChatMessage
import com.example.campuslink_android.core.network.TokenStore

class ChatMessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ChatMessage>()

    companion object {
        private const val TEXT_LEFT = 0
        private const val TEXT_RIGHT = 1
        private const val IMAGE_LEFT = 2
        private const val IMAGE_RIGHT = 3
    }

    override fun getItemViewType(position: Int): Int {
        val msg = items[position]
        val isMyMessage = msg.senderId == TokenStore.getUserId()

        return when (msg.messageType) {
            "IMAGE" -> if (isMyMessage) IMAGE_RIGHT else IMAGE_LEFT
            else -> if (isMyMessage) TEXT_RIGHT else TEXT_LEFT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            TEXT_LEFT -> {
                val view = inflater.inflate(R.layout.item_chat_text_left, parent, false)
                TextLeftViewHolder(view)
            }

            TEXT_RIGHT -> {
                val view = inflater.inflate(R.layout.item_chat_text_right, parent, false)
                TextRightViewHolder(view)
            }

            IMAGE_LEFT -> {
                val view = inflater.inflate(R.layout.item_chat_image_left, parent, false)
                ImageLeftViewHolder(view)
            }

            else -> { // IMAGE_RIGHT
                val view = inflater.inflate(R.layout.item_chat_image_right, parent, false)
                ImageRightViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = items[position]

        when (holder) {
            is TextLeftViewHolder -> holder.bind(msg)
            is TextRightViewHolder -> holder.bind(msg)
            is ImageLeftViewHolder -> holder.bind(msg)
            is ImageRightViewHolder -> holder.bind(msg)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list: List<ChatMessage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    // ---------------------------------------------------------
    // TEXT LEFT
    // ---------------------------------------------------------
    inner class TextLeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtMessage = itemView.findViewById<TextView>(R.id.txtMessage)
        private val txtTime = itemView.findViewById<TextView>(R.id.txtTime)

        fun bind(msg: ChatMessage) {
            txtMessage.text = msg.content
            txtTime.text = formatTime(msg.sentAt)
        }
    }

    // ---------------------------------------------------------
    // TEXT RIGHT
    // ---------------------------------------------------------
    inner class TextRightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtMessage = itemView.findViewById<TextView>(R.id.txtMessage)
        private val txtTime = itemView.findViewById<TextView>(R.id.txtTime)

        fun bind(msg: ChatMessage) {
            txtMessage.text = msg.content
            txtTime.text = formatTime(msg.sentAt)
        }
    }

    // ---------------------------------------------------------
    // IMAGE LEFT
    // ---------------------------------------------------------
    inner class ImageLeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgChat = itemView.findViewById<ImageView>(R.id.imgChat)
        private val txtTime = itemView.findViewById<TextView>(R.id.txtTime)

        fun bind(msg: ChatMessage) {
            val url = "http://10.0.2.2:8080" + msg.content
            Glide.with(itemView.context).load(url).into(imgChat)
            txtTime.text = formatTime(msg.sentAt)
        }
    }

    // ---------------------------------------------------------
    // IMAGE RIGHT
    // ---------------------------------------------------------
    inner class ImageRightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgChat = itemView.findViewById<ImageView>(R.id.imgChat)
        private val txtTime = itemView.findViewById<TextView>(R.id.txtTime)

        fun bind(msg: ChatMessage) {
            val url = "http://10.0.2.2:8080" + msg.content
            Glide.with(itemView.context).load(url).into(imgChat)
            txtTime.text = formatTime(msg.sentAt)
        }
    }

    // ---------------------------------------------------------
    // 시간 포맷 변환 (2025-12-02T03:10:02 → 03:10)
    // ---------------------------------------------------------
    private fun formatTime(sentAt: String): String {
        return try {
            sentAt.substring(11, 16) // HH:mm만 추출
        } catch (e: Exception) {
            ""
        }
    }
}
