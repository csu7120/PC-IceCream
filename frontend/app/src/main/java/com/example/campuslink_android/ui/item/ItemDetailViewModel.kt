package com.example.campuslink_android.ui.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore // ⭐️ [추가] TokenStore import
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.data.repository.RentalRepositoryImpl
import com.example.campuslink_android.domain.repository.RentalRepository
import kotlinx.coroutines.launch

class ItemDetailViewModel(
    private val rentalRepository: RentalRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun requestRental(itemId: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                rentalRepository.requestRental(itemId)
                _success.value = true
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _success.value = false
                _error.value = e.message ?: "대여 요청 실패"
            } finally {
                _loading.value = false
            }
        }
    }

    companion object {
        fun create(): ItemDetailViewModel {
            val api = ApiClient.create(RentalApi::class.java)
            // ⭐️ [수정] TokenStore 싱글톤 인스턴스를 함께 전달
            val repo: RentalRepository = RentalRepositoryImpl(api, TokenStore)
            return ItemDetailViewModel(repo)
        }
    }
}