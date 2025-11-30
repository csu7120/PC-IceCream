package com.example.campuslink_android.ui.rental

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.RentalRepository   // 🔥 여기 중요
import kotlinx.coroutines.launch

class RentalListViewModel(
    private val rentalRepository: RentalRepository
) : ViewModel() {

    private val _list = MutableLiveData<List<RentalResponseDto>>()
    val list: LiveData<List<RentalResponseDto>> = _list

    /* 내가 빌려준 목록 */
    fun loadRequestedRentals() {
        viewModelScope.launch {
            val result = rentalRepository.getRequestedRentals()
            _list.value = result
        }
    }

    /* 내가 빌린 목록 */
    fun loadMyRentals() {
        viewModelScope.launch {
            val result = rentalRepository.getMyRentals()
            _list.value = result
        }
    }
}
