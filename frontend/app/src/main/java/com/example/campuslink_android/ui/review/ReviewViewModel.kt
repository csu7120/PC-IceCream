package com.example.campuslink_android.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.ReviewRepository
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val repo: ReviewRepository
) : ViewModel() {

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun submitReview(rental: RentalResponseDto, rating: Double, comment: String, roleType: String) {
        viewModelScope.launch {
            try {
                repo.createReview(rental, rating, comment, roleType)
                _success.value = true
            } catch (e: Exception) {
                _success.value = false
            }
        }
    }
}
