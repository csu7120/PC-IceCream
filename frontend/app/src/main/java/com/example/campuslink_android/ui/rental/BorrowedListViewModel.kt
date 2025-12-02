package com.example.campuslink_android.ui.rental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.RentalRepository
import kotlinx.coroutines.launch

class BorrowedListViewModel(
    private val repository: RentalRepository
) : ViewModel() {

    private val _list = MutableLiveData<List<RentalResponseDto>>()
    val list: LiveData<List<RentalResponseDto>> = _list

    // ⭐ 내가 빌린 목록 로드
    fun loadMyRentals() {
        viewModelScope.launch {
            try {
                val rentals = repository.getMyRentals()
                _list.value = rentals
            } catch (e: Exception) {
                e.printStackTrace()
                _list.value = emptyList()
            }
        }
    }
}
