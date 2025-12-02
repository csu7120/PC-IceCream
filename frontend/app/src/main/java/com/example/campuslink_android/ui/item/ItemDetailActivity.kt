package com.example.campuslink_android.ui.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide // ⭐ Glide 라이브러리 (이미지 로딩용)
import com.example.campuslink_android.R
import com.example.campuslink_android.core.network.TokenStore
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

        // 1. Intent로 데이터 받기 (설명, 이미지 URL 추가)
        val itemId = intent.getIntExtra(EXTRA_ITEM_ID, -1)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val category = intent.getStringExtra(EXTRA_CATEGORY) ?: ""
        val ownerId = intent.getIntExtra(EXTRA_OWNER_ID, -1)
        val ownerName = intent.getStringExtra(EXTRA_OWNER_NAME) ?: ""
        val price = intent.getDoubleExtra(EXTRA_PRICE, 0.0)
        val status = intent.getStringExtra(EXTRA_STATUS)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: "상세 설명이 없습니다." // ⭐ 추가
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL) // ⭐ 추가

        // 2. 뷰 연결
        val imgItem = findViewById<ImageView>(R.id.imgItemDetail) // ⭐ 추가
        val txtTitle = findViewById<TextView>(R.id.txtItemTitle)
        val txtCategory = findViewById<TextView>(R.id.txtItemCategory)
        val txtOwner = findViewById<TextView>(R.id.txtItemOwner)
        val txtPrice = findViewById<TextView>(R.id.txtItemPrice)
        val txtDesc = findViewById<TextView>(R.id.txtDescription) // ⭐ 추가
        val btnRent = findViewById<Button>(R.id.btnRent)
        val btnChat = findViewById<Button>(R.id.btnChat)
        val btnBack = findViewById<ImageView>(R.id.btnBack) // 뒤로가기

        // 3. 화면에 데이터 뿌리기
        txtTitle.text = title
        txtCategory.text = "카테고리: $category"
        txtOwner.text = ownerName
        txtPrice.text = "${price.toInt()}원" // 보기 좋게 정수형 변환
        txtDesc.text = description // ⭐ 설명 자동 입력

        // ⭐ 이미지 로딩 (Glide 사용 추천)
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background) // 로딩 중 이미지
                .into(imgItem)
        } else {
            imgItem.setImageResource(R.drawable.ic_launcher_background) // 이미지 없을 때
        }

        // 뒤로가기 버튼
        btnBack.setOnClickListener { finish() }

        // ... (나머지 대여/채팅 버튼 로직은 기존 유지) ...

        if (status == "RENTED") {
            btnRent.isEnabled = false
            btnRent.text = "이미 대여 중"
        }

        btnRent.setOnClickListener {
            if (itemId == -1) return@setOnClickListener
            itemViewModel.requestRental(itemId)
        }

        itemViewModel.success.observe(this) { ok ->
            if (ok) Toast.makeText(this, "대여 요청 성공!", Toast.LENGTH_SHORT).show()
        }

        btnChat.setOnClickListener {
            if (ownerId == -1 || itemId == -1) return@setOnClickListener
            chatViewModel.openChat(ownerId)
        }

        chatViewModel.roomId.observe(this) { roomId ->
            if (roomId != null) ChatRoomActivity.start(this, roomId)
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
        private const val EXTRA_DESCRIPTION = "extra_description" // ⭐
        private const val EXTRA_IMAGE_URL = "extra_image_url" // ⭐

        // HomeFragment 등에서 호출할 때 이 함수를 사용하세요.
        fun start(context: Context, item: Item) {
            val intent = Intent(context, ItemDetailActivity::class.java).apply {
                putExtra(EXTRA_ITEM_ID, item.id)
                putExtra(EXTRA_TITLE, item.title)
                putExtra(EXTRA_CATEGORY, item.category)
                putExtra(EXTRA_OWNER_ID, item.ownerId)
                putExtra(EXTRA_OWNER_NAME, item.ownerName)
                putExtra(EXTRA_PRICE, item.price)
                putExtra(EXTRA_STATUS, item.status)
                putExtra(EXTRA_DESCRIPTION, item.description) // ⭐ Item 모델에 description이 있어야 함
                putExtra(EXTRA_IMAGE_URL, item.imageUrl) // ⭐ Item 모델에 imageUrl이 있어야 함
            }
            context.startActivity(intent)
        }
    }
}