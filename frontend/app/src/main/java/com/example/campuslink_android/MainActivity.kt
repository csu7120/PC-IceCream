package com.example.campuslink_android

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.remote.auth.LoginRequest
import com.example.campuslink_android.data.remote.user.UserApi
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var api: UserApi
    private lateinit var store: TokenStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store = TokenStore(this)
        val BASE_URL = "http://10.0.2.2:8080/api/"
        api = ApiClient.get(this, BASE_URL).create(UserApi::class.java)

        val req = LoginRequest(email = "test@school.ac.kr", password = "password123")

        lifecycleScope.launch {
            try {
                val res = api.login(req)
                if (res.isSuccessful && (res.body()?.success == true)) {
                    val token = res.body()!!.data.token
                    store.save(token)
                    Toast.makeText(this@MainActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    loadMe()
                } else {
                    Toast.makeText(this@MainActivity, "로그인 실패 (${res.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadMe() {
        lifecycleScope.launch {
            try {
                val res = api.getMyInfo()
                if (res.isSuccessful && (res.body()?.success == true)) {
                    val u = res.body()!!.data
                    Log.d("ME", "userId=${u.userId}, name=${u.name}, email=${u.email}")
                    Toast.makeText(this@MainActivity, "ME: ${u.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "me() 실패 (${res.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("ME", "에러", e)
                Toast.makeText(this@MainActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
