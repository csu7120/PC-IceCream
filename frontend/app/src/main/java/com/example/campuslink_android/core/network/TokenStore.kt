package com.example.campuslink_android.core.network

object TokenStore {

    @Volatile
    private var token: String? = null

    @Volatile
    private var expiresAtMillis: Long? = null

    @Volatile
    private var userId: Int? = null

    @Volatile
    private var email: String? = null

    fun saveLoginInfo(token: String, expiresInMinutes: Int, userId: Int) {
        this.token = token
        this.expiresAtMillis = System.currentTimeMillis() + expiresInMinutes * 60_000L
        this.userId = userId
    }

    fun saveEmail(email: String) {
        this.email = email
    }

    fun getToken(): String? {
        val exp = expiresAtMillis ?: return null
        if (System.currentTimeMillis() > exp) {
            clear()
            return null
        }
        return token
    }

    fun getUserId(): Int? = userId

    fun getEmail(): String? = email

    fun clear() {
        token = null
        expiresAtMillis = null
        userId = null
        email = null
    }
}
