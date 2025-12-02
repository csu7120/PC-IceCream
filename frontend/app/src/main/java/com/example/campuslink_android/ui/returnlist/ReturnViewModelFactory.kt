package com.example.campuslink_android.ui.returnlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.domain.repository.RentalRepository

class ReturnViewModelFactory(
    private val repo: RentalRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReturnViewModel::class.java)) {
            return ReturnViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
