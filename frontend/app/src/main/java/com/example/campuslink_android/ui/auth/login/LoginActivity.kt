package com.example.campuslink_android.ui.auth.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.campuslink_android.data.remote.auth.LoginRequest
import com.example.campuslink_android.theme.IceCreamTheme
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IceCreamTheme {
                LoginScreen(
                    onLoginClick = { email, pw ->
                        doLogin(email, pw)
                    }
                )
            }
        }
    }

    private fun doLogin(email: String, password: String) {
        val req = LoginRequest(email, password)

        lifecycleScope.launch {
            val result = viewModel.login(req)

            if (result?.success == true) {
                Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
