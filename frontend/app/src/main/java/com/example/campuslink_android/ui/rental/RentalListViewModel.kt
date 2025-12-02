package com.example.campuslink_android.ui.rental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.RentalRepository
import kotlinx.coroutines.launch

class RentalListViewModel(
    private val repository: RentalRepository
) : ViewModel() {

    private val _list = MutableLiveData<List<RentalResponseDto>>()
    val list: LiveData<List<RentalResponseDto>> = _list

    // ⭐ 수락 성공 여부 LiveData
    private val _acceptResult = MutableLiveData<Boolean>()
    val acceptResult: LiveData<Boolean> = _acceptResult

    fun loadRequestedRentals() {
        viewModelScope.launch {
            val result = repository.getRequestedRentals()
            _list.value = result
        }
    }

    fun acceptRental(rentalId: Int) {
        viewModelScope.launch {
            try {
                repository.acceptRental(rentalId)
                _acceptResult.postValue(true)   // ⭐ 성공 알림
            } catch (e: Exception) {
                e.printStackTrace()
                _acceptResult.postValue(false)  // 실패 알림
            }
        }
    }
}


