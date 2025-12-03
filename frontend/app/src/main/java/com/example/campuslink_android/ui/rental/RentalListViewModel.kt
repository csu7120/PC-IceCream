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

    // ⭐ 수락 성공 여부 LiveData (요청 목록 화면에서 사용)
    private val _acceptResult = MutableLiveData<Boolean>()
    val acceptResult: LiveData<Boolean> = _acceptResult

    /** 대여 요청 목록 (REQUESTED 상태) */
    fun loadRequestedRentals() {
        viewModelScope.launch {
            val result = repository.getRequestedRentals()
            _list.value = result
        }
    }

    /** 내가 빌려준 목록 (REQUESTED 제외) */
    fun loadLentRentals() {
        viewModelScope.launch {
            val result = repository.getLentRentals()
            _list.value = result
        }
    }

    /** 대여 수락 */
    fun acceptRental(rentalId: Int) {
        viewModelScope.launch {
            try {
                repository.acceptRental(rentalId)
                _acceptResult.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _acceptResult.postValue(false)
            }
        }
    }
}
