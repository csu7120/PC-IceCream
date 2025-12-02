package com.example.campuslink_android.ui.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.campuslink_android.R
import com.example.campuslink_android.domain.model.Item
import com.example.campuslink_android.ui.chat.ChatRoomActivity
import com.example.campuslink_android.ui.chat.ChatViewModel
import com.example.campuslink_android.ui.chat.ChatViewModelFactory

class ItemDetailActivity : AppCompatActivity() {

    private val itemViewModel: ItemDetailViewModel by lazy {
        ItemDetailViewModel.create()
    }

    private val chatViewModel: ChatViewModel by lazy {
        androidx.lifecycle.ViewModelProvider(this, ChatViewModelFactory())[ChatViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        // ✔ 전달받은 데이터
        val itemId = intent.getIntExtra(EXTRA_ITEM_ID, -1)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val category = intent.getStringExtra(EXTRA_CATEGORY) ?: ""
        val ownerId = intent.getIntExtra(EXTRA_OWNER_ID, -1)
        val ownerName = intent.getStringExtra(EXTRA_OWNER_NAME) ?: ""
        val price = intent.getDoubleExtra(EXTRA_PRICE, 0.0)
        val status = intent.getStringExtra(EXTRA_STATUS)

        // ✔ UI 요소 연결
        val txtTitle = findViewById<TextView>(R.id.txtItemTitle)
        val txtCategory = findViewById<TextView>(R.id.txtItemCategory)
        val txtOwner = findViewById<TextView>(R.id.txtItemOwner)
        val txtPrice = findViewById<TextView>(R.id.txtItemPrice)
        val btnRent = findViewById<Button>(R.id.btnRent)
        val btnChat = findViewById<Button>(R.id.btnChat)

        // ✔ 화면에 표시
        txtTitle.text = title
        txtCategory.text = "카테고리: $category"
        txtOwner.text = "등록자: $ownerName"
        txtPrice.text = "가격: ${price}원"

        // ✔ 대여 중 표시
        if (status == "RENTED") {
            btnRent.isEnabled = false
            btnRent.text = "이미 대여 중"
        }

        // ------------------------------------
        // 1) 대여 요청
        // ------------------------------------
        btnRent.setOnClickListener {
            if (itemId == -1) {
                Toast.makeText(this, "itemId가 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            itemViewModel.requestRental(itemId)
        }

        itemViewModel.success.observe(this) { ok ->
            if (ok == true) {
                Toast.makeText(this, "대여 요청 성공!", Toast.LENGTH_SHORT).show()
            }
        }

        itemViewModel.error.observe(this) { msg ->
            if (!msg.isNullOrBlank()) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }

        // ------------------------------------
        // 2) 채팅하기
        // ------------------------------------
        btnChat.setOnClickListener {
            android.util.Log.d("ChatDebug", "채팅 클릭 → 현재 토큰 = ${TokenStore.getToken()}")
            android.util.Log.d("ChatDebug", "현재 userId = ${TokenStore.getUserId()}")

            if (ownerId == -1) {
                Toast.makeText(this, "판매자 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (itemId == -1) {
                Toast.makeText(this, "아이템 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "채팅방 생성 중...", Toast.LENGTH_SHORT).show()

            // ownerId + itemId 로 채팅방 생성
            chatViewModel.openChat(ownerId)
        }

        // ✔ 채팅방 생성 완료 시 → 채팅방 화면 이동
        chatViewModel.roomId.observe(this, Observer { roomId ->
            if (roomId != null) {
                ChatRoomActivity.start(this, roomId)
            }
        })
    }

    companion object {
        private const val EXTRA_ITEM_ID = "extra_item_id"
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_CATEGORY = "extra_category"
        private const val EXTRA_OWNER_ID = "extra_owner_id"
        private const val EXTRA_OWNER_NAME = "extra_owner_name"
        private const val EXTRA_PRICE = "extra_price"
        private const val EXTRA_STATUS = "extra_status"

        fun start(context: Context, item: Item) {
            val intent = Intent(context, ItemDetailActivity::class.java).apply {
                putExtra(EXTRA_ITEM_ID, item.id)
                putExtra(EXTRA_TITLE, item.title)
                putExtra(EXTRA_CATEGORY, item.category)
                putExtra(EXTRA_OWNER_ID, item.ownerId)
                putExtra(EXTRA_OWNER_NAME, item.ownerName)
                putExtra(EXTRA_PRICE, item.price)
                putExtra(EXTRA_STATUS, item.status)
            }
            context.startActivity(intent)
        }
    }
}
