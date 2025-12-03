package com.example.campuslink_android.core.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object TokenStore {

    private const val PREF_NAME = "auth_pref"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_EXPIRES = "jwt_expires"
    private const val KEY_USER_ID = "jwt_userId"
    private const val KEY_EMAIL = "jwt_email"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // -----------------------------
    // 로그인 정보 저장
    // -----------------------------
    fun saveLoginInfo(token: String, expiresInMinutes: Int, userId: Int) {
        val expiresAtMillis = System.currentTimeMillis() + expiresInMinutes * 60_000L

        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putLong(KEY_EXPIRES, expiresAtMillis)
            .putInt(KEY_USER_ID, userId)
            .apply()

        Log.d("TokenStore", "saveLoginInfo() → token=$token, userId=$userId")
    }

    // -----------------------------
    // 이메일 저장 / 불러오기
    // -----------------------------
    fun saveEmail(email: String) {
        prefs.edit().putString(KEY_EMAIL, email).apply()
        Log.d("TokenStore", "saveEmail() → email=$email")
    }

    fun getEmail(): String? {
        val email = prefs.getString(KEY_EMAIL, null)
        Log.d("TokenStore", "getEmail() → $email")
        return email
    }

    // -----------------------------
    // 토큰 가져오기
    // -----------------------------
    fun getToken(): String? {
        val token = prefs.getString(KEY_TOKEN, null)
        val expires = prefs.getLong(KEY_EXPIRES, -1L)

        Log.d("TokenStore", "getToken() → token=$token, expires=$expires")

        if (token.isNullOrBlank()) return null

        if (expires != -1L && System.currentTimeMillis() > expires) {
            Log.d("TokenStore", "토큰 만료됨 → clear()")
            clear()
            return null
        }

        return token
    }

    // -----------------------------
    // userId 가져오기
    // -----------------------------
    fun getUserId(): Int? {
        val id = prefs.getInt(KEY_USER_ID, -1)
        return if (id == -1) null else id
    }

    // -----------------------------
    // 전체 삭제
    // -----------------------------
    fun clear() {
        prefs.edit().clear().apply()
    }
}
