package com.example.campuslink_android.ui.returnlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.RentalRepository
import kotlinx.coroutines.launch

class ReturnViewModel(
    private val repo: RentalRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<RentalResponseDto>>()
    val items: LiveData<List<RentalResponseDto>> = _items

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result

    // ⭐ 방금 반납된 rental 정보를 저장해서 팝업 띄울 때 사용
    var lastReturnedRental: RentalResponseDto? = null
        private set

    fun loadReturnableItems() {
        viewModelScope.launch {
            val list = repo.getMyRentals()
            _items.value = list.filter {
                it.status == "PICKED_UP" || it.status == "IN_USE" || it.status == "LATE"
            }
        }
    }

    fun returnItem(rental: RentalResponseDto) {
        viewModelScope.launch {
            try {
                // 서버 반납 요청
                repo.returnRental(rental.rentalId)

                // 팝업 띄우기 위해 현재 rental 저장
                lastReturnedRental = rental

                _result.value = true
            } catch (e: Exception) {
                _result.value = false
            }
        }
    }
}
