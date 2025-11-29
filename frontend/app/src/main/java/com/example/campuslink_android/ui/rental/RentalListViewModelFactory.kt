package com.example.campuslink_android.ui.rental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.domain.repository.RentalRepository

class RentalListViewModelFactory(
    private val rentalRepository: RentalRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RentalListViewModel::class.java)) {
            return RentalListViewModel(rentalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
