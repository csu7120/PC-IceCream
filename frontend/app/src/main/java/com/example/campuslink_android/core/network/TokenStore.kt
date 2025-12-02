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
        android.util.Log.d(
            "TokenStore",
            "saveLoginInfo() 저장됨 → token=$token, userId=$userId, expiresAt=$expiresAtMillis"
        )
    }

    fun saveEmail(email: String) {
        this.email = email
    }

    fun getToken(): String? {
        android.util.Log.d("TokenStore", "getToken() 호출됨 → token=$token, exp=$expiresAtMillis")

        // 토큰 자체가 없으면 null
        if (token.isNullOrBlank()) return null

        // 만료시간이 존재할 때만 체크
        expiresAtMillis?.let { exp ->
            if (System.currentTimeMillis() > exp) {
                android.util.Log.d("TokenStore", "토큰 만료됨 → clear() 호출")
                clear()
                return null
            }
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
