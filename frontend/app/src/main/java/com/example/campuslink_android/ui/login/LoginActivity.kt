package com.example.campuslink_android.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.campuslink_android.MainActivity
import com.example.campuslink_android.R

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by lazy {
        LoginViewModel.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEdit = findViewById<EditText>(R.id.editEmail)
        val passEdit = findViewById<EditText>(R.id.editPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)

        loginBtn.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passEdit.text.toString()

            Toast.makeText(this, "로그인 버튼 눌림", Toast.LENGTH_SHORT).show()

            viewModel.login(email, password)
        }

        viewModel.loginState.observe(this, Observer { state ->
            when (state) {
                is LoginViewModel.LoginState.Idle -> {
                }
                is LoginViewModel.LoginState.Loading -> {
                }
                is LoginViewModel.LoginState.Success -> {
                    Toast.makeText(
                        this,
                        "${state.user.name} 님 환영합니다!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                is LoginViewModel.LoginState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
