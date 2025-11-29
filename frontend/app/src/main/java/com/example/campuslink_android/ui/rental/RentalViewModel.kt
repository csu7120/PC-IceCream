package com.example.campuslink_android.ui.rental

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.domain.repository.RentalRepository
import kotlinx.coroutines.launch

class RentalViewModel(
    private val rentalRepository: RentalRepository
) : ViewModel() {

    private val _acceptResult = MutableLiveData<Result<Unit>>()
    val acceptResult: LiveData<Result<Unit>> = _acceptResult

    fun acceptRental(rentalId: Int) {
        viewModelScope.launch {
            runCatching {
                rentalRepository.acceptRental(rentalId)
            }.onSuccess {
                _acceptResult.value = Result.success(Unit)
            }.onFailure { e ->
                _acceptResult.value = Result.failure(e)
            }
        }
    }
}
