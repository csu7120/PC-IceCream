package com.example.campuslink_android.ui.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
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
        ViewModelProvider(this, ChatViewModelFactory())[ChatViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        // 1. Intent로 데이터 받기 (🔥 예전처럼 전부 Intent에서 가져옴)
        val itemId = intent.getIntExtra(EXTRA_ITEM_ID, -1)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val category = intent.getStringExtra(EXTRA_CATEGORY) ?: ""
        val ownerId = intent.getIntExtra(EXTRA_OWNER_ID, -1)
        val ownerName = intent.getStringExtra(EXTRA_OWNER_NAME) ?: ""
        val price = intent.getDoubleExtra(EXTRA_PRICE, 0.0)
        val status = intent.getStringExtra(EXTRA_STATUS)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: "상세 설명이 없습니다."
        val rawImageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)

        // 2. 뷰 연결
        val imgItem = findViewById<ImageView>(R.id.imgItemDetail)
        val txtTitle = findViewById<TextView>(R.id.txtItemTitle)
        val txtCategory = findViewById<TextView>(R.id.txtItemCategory)
        val txtOwner = findViewById<TextView>(R.id.txtItemOwner)
        val txtPrice = findViewById<TextView>(R.id.txtItemPrice)
        val txtDesc = findViewById<TextView>(R.id.txtDescription)
        val btnRent = findViewById<Button>(R.id.btnRent)
        val btnChat = findViewById<Button>(R.id.btnChat)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // 3. 텍스트 데이터 세팅
        txtTitle.text = title
        txtCategory.text = "카테고리: $category"
        txtOwner.text = ownerName
        txtPrice.text = "${price.toInt()}원"
        txtDesc.text = description

        // 4. 이미지 로딩 (예전 방식 그대로)
        if (!rawImageUrl.isNullOrEmpty()) {
            val fullUrl = if (rawImageUrl.startsWith("/uploads/")) {
                // 에뮬레이터 기준 백엔드 주소
                "http://10.0.2.2:8080$rawImageUrl"
            } else {
                rawImageUrl
            }

            Glide.with(this)
                .load(fullUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imgItem)
        } else {
            imgItem.setImageResource(R.drawable.ic_launcher_background)
        }

        // 5. 뒤로가기
        btnBack.setOnClickListener { finish() }

        // 6. 대여 불가 상태 처리
        if (status == "RENTED") {
            btnRent.isEnabled = false
            btnRent.text = "이미 대여 중"
        }

        // 7. 대여 요청
        btnRent.setOnClickListener {
            if (itemId == -1) return@setOnClickListener
            itemViewModel.requestRental(itemId)
        }

        itemViewModel.success.observe(this) { ok ->
            if (ok == true) {
                Toast.makeText(this, "대여 요청 성공!", Toast.LENGTH_SHORT).show()
            }
        }

        // 8. 채팅 열기 (🔥 예전처럼 ownerId 그대로 사용)
        btnChat.setOnClickListener {
            if (ownerId == -1 || itemId == -1) return@setOnClickListener
            chatViewModel.openChat(ownerId)
        }

        chatViewModel.roomId.observe(this) { roomId ->
            if (roomId != null) {
                ChatRoomActivity.start(this, roomId)
            }
        }
    }

    companion object {
        private const val EXTRA_ITEM_ID = "extra_item_id"
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_CATEGORY = "extra_category"
        private const val EXTRA_OWNER_ID = "extra_owner_id"
        private const val EXTRA_OWNER_NAME = "extra_owner_name"
        private const val EXTRA_PRICE = "extra_price"
        private const val EXTRA_STATUS = "extra_status"
        private const val EXTRA_DESCRIPTION = "extra_description"
        private const val EXTRA_IMAGE_URL = "extra_image_url"

        // 🔥 예전처럼 홈/리스트에서 Item 전체를 넘길 때 쓰는 함수
        fun start(context: Context, item: Item) {
            val intent = Intent(context, ItemDetailActivity::class.java).apply {
                putExtra(EXTRA_ITEM_ID, item.id)
                putExtra(EXTRA_TITLE, item.title)
                putExtra(EXTRA_CATEGORY, item.category)
                putExtra(EXTRA_OWNER_ID, item.ownerId)
                putExtra(EXTRA_OWNER_NAME, item.ownerName)
                putExtra(EXTRA_PRICE, item.price)
                putExtra(EXTRA_STATUS, item.status)
                putExtra(EXTRA_DESCRIPTION, item.description)
                putExtra(EXTRA_IMAGE_URL, item.thumbnailUrl ?: item.imageUrl)
            }
            context.startActivity(intent)
        }
    }
}
