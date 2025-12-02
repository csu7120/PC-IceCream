package com.example.campuslink_android.ui.rental

import androidx.lifecycle.*
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.RentalRepository
import com.example.campuslink_android.core.network.TokenStore
import kotlinx.coroutines.launch
import android.util.Log
class RentalViewModel(
    private val rentalRepository: RentalRepository
) : ViewModel() {

    private val _acceptResult = MutableLiveData<Result<Unit>>()
    val acceptResult: LiveData<Result<Unit>> = _acceptResult

    fun acceptRental(rentalId: Int) {
        Log.e("DEBUG_VM", "acceptRental() called with rentalId=$rentalId")
        viewModelScope.launch {
            runCatching {
                rentalRepository.acceptRental(rentalId)
            }.onSuccess {
                _acceptResult.value = Result.success(Unit)
            }.onFailure { e ->
                Log.e("DEBUG_VM", "Failed: ${e.message}")
                _acceptResult.value = Result.failure(e)
            }
        }
    }



    // 리스트 로드
    private val _requestedRentals = MutableLiveData<List<RentalResponseDto>>()
    val requestedRentals: LiveData<List<RentalResponseDto>> = _requestedRentals

    fun loadRequestedRentals() {
        viewModelScope.launch {
            runCatching {
                rentalRepository.getRequestedRentals()
            }.onSuccess {
                _requestedRentals.value = it
            }.onFailure {
                _requestedRentals.value = emptyList()
            }
        }
    }
}

