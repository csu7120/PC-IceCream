package com.example.campuslink_android.ui.splashLogo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.campuslink_android.R
import com.example.campuslink_android.ui.auth.login.LoginActivity

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val btnStart = findViewById<Button>(R.id.btnStart)

        // ✅ 버튼은 남겨둬도 되고 눌러도 정상 이동됨
        btnStart.setOnClickListener {
            moveToNext()
        }

        // ✅ 앱 켜지면 자동으로 다음 화면 이동 (3초 후)
        Handler(Looper.getMainLooper()).postDelayed({
            moveToNext()
        }, 2500) // 1.5초 후 자동 이동
    }

    private fun moveToNext() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // 인트로 화면 종료해서 뒤로가기 안 뜨게
    }
}
