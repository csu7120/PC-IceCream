package com.example.campuslink_android.ui.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.data.repository.RentalRepositoryImpl
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.domain.repository.RentalRepository
import kotlinx.coroutines.launch

class ItemDetailViewModel(
    private val rentalRepository: RentalRepository
) : ViewModel() {

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun requestRental(itemId: Int) {
        viewModelScope.launch {
            runCatching {
                rentalRepository.requestRental(itemId)
            }.onSuccess {
                _success.value = true
            }.onFailure {
                _error.value = it.message ?: "대여 요청 실패"
            }
        }
    }

    companion object {
        fun create(): ItemDetailViewModel {
            val repository = RentalRepositoryImpl(
                rentalApi = ApiClient.create(RentalApi::class.java),
                tokenStore = TokenStore
            )

            return ItemDetailViewModel(repository)
        }
    }
}
