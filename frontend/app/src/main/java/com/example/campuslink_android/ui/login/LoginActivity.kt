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
import com.example.campuslink_android.ui.signup.SignupActivity

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by lazy {
        LoginViewModel.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 🔥 XML에서 실제 존재하는 ID로 맞춰라!
        val emailEdit = findViewById<EditText>(R.id.editEmail)
        val passEdit = findViewById<EditText>(R.id.editPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val signupBtn = findViewById<Button>(R.id.btnSignup)

        // ▶ 로그인 버튼
        loginBtn.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passEdit.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        // ▶ 회원가입 이동
        signupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // ▶ 로그인 결과 처리
        viewModel.loginState.observe(this, Observer { state ->
            when (state) {
                is LoginViewModel.LoginState.Idle -> {}

                is LoginViewModel.LoginState.Loading -> {}

                is LoginViewModel.LoginState.Success -> {
                    Toast.makeText(
                        this, "${state.user.name} 님 환영합니다!",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is LoginViewModel.LoginState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
