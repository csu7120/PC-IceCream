package com.example.campuslink_android.ui.common

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.data.dao.ReportApi
import com.example.campuslink_android.data.repository.ReportRepositoryImpl
import com.example.campuslink_android.ui.report.ReportViewModel

// Context를 받도록 해두면 나중에 로컬 DB나 SharedPreference 쓸 때도 편합니다.
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 1. ReportViewModel을 요청했을 때
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            // (1) API 객체 생성 (Retrofit)
            val api = ApiClient.create(ReportApi::class.java)

            // (2) Repository 생성 (API 주입)
            val repository = ReportRepositoryImpl(api)

            // (3) ViewModel 생성 (Repository 주입) 및 반환
            @Suppress("UNCHECKED_CAST")
            return ReportViewModel(repository) as T
        }

        // 나중에 ChatViewModel, HomeViewModel 등도 여기서 else if로 추가하면 됩니다.
        /*
        else if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            ...
        }
        */

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}