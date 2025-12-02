package com.example.campuslink_android.ui.pickup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.domain.repository.RentalRepository

class PickupViewModelFactory(
    private val repo: RentalRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PickupViewModel::class.java)) {
            return PickupViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
