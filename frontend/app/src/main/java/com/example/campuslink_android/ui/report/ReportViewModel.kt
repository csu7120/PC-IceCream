package com.example.campuslink_android.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.domain.repository.ReportRepository
import kotlinx.coroutines.launch

class ReportViewModel(private val repository: ReportRepository) : ViewModel() {

    private val _reportResult = MutableLiveData<Boolean>()
    val reportResult: LiveData<Boolean> get() = _reportResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // 신고하기 함수
    fun reportUser(reporterId: Int, targetUserId: Int, reason: String) {
        viewModelScope.launch {
            val result = repository.createReport(reporterId, targetUserId, reason)
            if (result.isSuccess) {
                _reportResult.value = true
            } else {
                _reportResult.value = false
                _errorMessage.value = result.exceptionOrNull()?.message ?: "오류 발생"
            }
        }
    }
}