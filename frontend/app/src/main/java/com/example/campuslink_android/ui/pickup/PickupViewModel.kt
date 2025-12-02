package com.example.campuslink_android.ui.pickup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.RentalRepository
import kotlinx.coroutines.launch

class PickupViewModel(
    private val repo: RentalRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<RentalResponseDto>>()
    val items: LiveData<List<RentalResponseDto>> = _items

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result

    // ⭐ ACCEPTED 상태(픽업 가능)
    fun loadAcceptedItems() {
        viewModelScope.launch {
            val list = repo.getMyRentals()
            _items.value = list.filter { it.status == "ACCEPTED" }
        }
    }

    // ⭐ PICKED_UP 상태(반납 가능)
    fun loadPickedUpItems() {
        viewModelScope.launch {
            val list = repo.getMyRentals()
            _items.value = list.filter {
                it.status == "PICKED_UP" || it.status == "IN_USE"
            }
        }
    }

    fun pickup(rentalId: Int) {
        viewModelScope.launch {
            try {
                repo.pickupRental(rentalId)
                _result.value = true
            } catch (e: Exception) {
                _result.value = false
            }
        }
    }

    fun returnItem(rentalId: Int) {
        viewModelScope.launch {
            try {
                repo.returnRental(rentalId)
                _result.value = true
            } catch (e: Exception) {
                _result.value = false
            }
        }
    }
}
