package com.example.campuslink_android.ui.splashLogo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.campuslink_android.R
import com.example.campuslink_android.ui.auth.AuthEntryActivity

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val btnStart = findViewById<Button>(R.id.btnStart)

        btnStart.setOnClickListener {
            val intent = Intent(this, AuthEntryActivity::class.java)
            startActivity(intent)
        }
    }
}
