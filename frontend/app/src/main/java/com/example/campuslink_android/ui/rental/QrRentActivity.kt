package com.example.campuslink_android.ui.rental

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.campuslink_android.R
import com.example.campuslink_android.ui.item.ItemDetailViewModel

class QrRentActivity : AppCompatActivity() {

    private val viewModel: ItemDetailViewModel by lazy {
        ItemDetailViewModel.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_rent)

        val editQr = findViewById<EditText>(R.id.editQr)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val txtResult = findViewById<TextView>(R.id.txtResult)

        btnSend.setOnClickListener {
            val qrText = editQr.text.toString()
            val itemId = qrText.toIntOrNull()

            if (itemId != null) {
                viewModel.requestRental(itemId)
            } else {
                txtResult.text = "QR에서 itemId를 읽을 수 없습니다."
            }
        }

        viewModel.success.observe(this) { ok ->
            if (ok == true) {
                txtResult.text = "QR 대여 요청 성공!"
            }
        }

        viewModel.error.observe(this) { msg ->
            if (!msg.isNullOrBlank()) {
                txtResult.text = "오류: $msg"
            }
        }
    }
}
