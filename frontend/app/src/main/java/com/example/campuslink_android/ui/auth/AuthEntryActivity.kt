package com.example.campuslink_android.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.campuslink_android.R
import com.example.campuslink_android.ui.auth.login.LoginActivity
import com.example.campuslink_android.ui.signup.SignupActivity

class AuthEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_entry)

        val btnGoLogin = findViewById<Button>(R.id.btnGoLogin)
        val btnGoSignup = findViewById<Button>(R.id.btnGoSignup)

        btnGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnGoSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
