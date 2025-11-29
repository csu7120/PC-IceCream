package com.example.campuslink_android.ui.rental

import androidx.lifecycle.*
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.RentalRepository
import kotlinx.coroutines.launch

class RentalListViewModel(
    private val rentalRepository: RentalRepository
) : ViewModel() {

    private val _list = MutableLiveData<List<RentalResponseDto>>()
    val list: LiveData<List<RentalResponseDto>> = _list

    fun loadRequestedRentals() {
        viewModelScope.launch {
            val result = rentalRepository.getRequestedRentals()
            _list.value = result
        }
    }
}
