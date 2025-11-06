package com.example.campuslink_android.ui.auth.signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.campuslink_android.data.remote.auth.SignupRequest
import com.example.campuslink_android.theme.IceCreamTheme
import kotlinx.coroutines.launch

class SignupActivity : ComponentActivity() {

    private val viewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IceCreamTheme {
                SignupScreen { email, pw ->
                    doSignup(email, pw)
                }
            }
        }
    }

    private fun doSignup(email: String, pw: String) {
        val req = SignupRequest(email, pw)

        lifecycleScope.launch {
            val result = viewModel.signup(req)
            if (result?.success == true) {
                Toast.makeText(this@SignupActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@SignupActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
