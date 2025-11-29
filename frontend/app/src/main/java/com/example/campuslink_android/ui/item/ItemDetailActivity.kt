package com.example.campuslink_android.ui.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campuslink_android.R
import com.example.campuslink_android.domain.model.Item

class ItemDetailActivity : AppCompatActivity() {

    private val viewModel: ItemDetailViewModel by lazy {
        ItemDetailViewModel.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        val itemId = intent.getIntExtra(EXTRA_ITEM_ID, -1)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val category = intent.getStringExtra(EXTRA_CATEGORY) ?: ""
        val ownerName = intent.getStringExtra(EXTRA_OWNER) ?: ""
        val price = intent.getDoubleExtra(EXTRA_PRICE, 0.0)
        val status = intent.getStringExtra(EXTRA_STATUS)

        val txtTitle = findViewById<TextView>(R.id.txtItemTitle)
        val txtCategory = findViewById<TextView>(R.id.txtItemCategory)
        val txtOwner = findViewById<TextView>(R.id.txtItemOwner)
        val txtPrice = findViewById<TextView>(R.id.txtItemPrice)
        val btnRent = findViewById<Button>(R.id.btnRent)

        txtTitle.text = title
        txtCategory.text = "카테고리: $category"
        txtOwner.text = "등록자: $ownerName"
        txtPrice.text = "가격: ${price}원"

        if (status == "RENTED") {
            btnRent.isEnabled = false
            btnRent.text = "이미 대여 중"
        }

        btnRent.setOnClickListener {
            if (itemId != -1) {
                viewModel.requestRental(itemId)
            } else {
                Toast.makeText(this, "itemId가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.success.observe(this) { ok ->
            if (ok == true) {
                Toast.makeText(this, "대여 요청 성공!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(this) { msg ->
            if (!msg.isNullOrBlank()) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val EXTRA_ITEM_ID = "extra_item_id"
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_CATEGORY = "extra_category"
        private const val EXTRA_OWNER = "extra_owner"
        private const val EXTRA_PRICE = "extra_price"
        private const val EXTRA_STATUS = "extra_status"

        fun start(context: Context, item: Item) {
            val intent = Intent(context, ItemDetailActivity::class.java).apply {
                putExtra(EXTRA_ITEM_ID, item.id)
                putExtra(EXTRA_TITLE, item.title)
                putExtra(EXTRA_CATEGORY, item.category)
                putExtra(EXTRA_OWNER, item.ownerName)
                putExtra(EXTRA_PRICE, item.price)
                putExtra(EXTRA_STATUS, item.status)
            }
            context.startActivity(intent)
        }
    }
}
