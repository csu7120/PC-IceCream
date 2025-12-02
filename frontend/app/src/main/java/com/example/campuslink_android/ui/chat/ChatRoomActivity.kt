package com.example.campuslink_android.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.campuslink_android.R
import com.example.campuslink_android.ui.chat.ChatRoomFragment
class ChatRoomActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, roomId: Int) {
            val intent = Intent(context, ChatRoomActivity::class.java)
            intent.putExtra("roomId", roomId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        val roomId = intent.getIntExtra("roomId", 0)
        println("⚡ ChatRoomActivity opened with roomId = $roomId")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.chatRoomContainer, ChatRoomFragment.newInstance(roomId))
                .commit()
        }
    }
}
