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
    // TEXT LEFT (받은 텍스트 메시지)
    // ---------------------------------------------------------
    inner class TextLeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgProfile = itemView.findViewById<ImageView>(R.id.imgProfile)
        private val txtSenderName = itemView.findViewById<TextView>(R.id.tvSenderName)
        private val txtMessage = itemView.findViewById<TextView>(R.id.txtMessage)
        private val txtTime = itemView.findViewById<TextView>(R.id.txtTime)

        fun bind(msg: ChatMessage) {

            txtMessage.text = msg.content
            txtTime.text = formatTime(msg.sentAt)

            // 이름 (DTO에 senderName이 없으면 기본값)
            txtSenderName.text = msg.senderName ?: "상대방"

            // 프로필 이미지
            if (msg.profileUrl != null) {
                Glide.with(itemView.context)
                    .load(msg.profileUrl)
                    .circleCrop()
                    .into(imgProfile)
            } else {
                imgProfile.setImageResource(R.drawable.ic_profile_default)
            }
        }
    }

    // ---------------------------------------------------------
    // TEXT RIGHT (내 메시지)
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
    // IMAGE LEFT (받은 이미지)
    // ---------------------------------------------------------
    inner class ImageLeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgProfile = itemView.findViewById<ImageView>(R.id.imgProfile)
        private val imgChat = itemView.findViewById<ImageView>(R.id.imgChat)
        private val txtTime = itemView.findViewById<TextView>(R.id.txtTime)

        fun bind(msg: ChatMessage) {
            val url = "http://10.0.2.2:8080" + msg.content
            Glide.with(itemView.context).load(url).into(imgChat)

            txtTime.text = formatTime(msg.sentAt)

            // 프로필 이미지
            imgProfile.setImageResource(R.drawable.ic_profile_default)
        }
    }


    // ---------------------------------------------------------
    // IMAGE RIGHT (내 이미지)
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
    // 시간 포맷 변환
    // ---------------------------------------------------------
    private fun formatTime(sentAt: String): String {
        return try {
            sentAt.substring(11, 16)
        } catch (e: Exception) {
            ""
        }
    }
}
