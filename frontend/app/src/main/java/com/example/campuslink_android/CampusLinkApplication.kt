package com.example.campuslink_android

import android.app.Application
import com.example.campuslink_android.core.network.TokenStore

class CampusLinkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenStore.init(this)
    }
}
