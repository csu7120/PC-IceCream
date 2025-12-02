package com.example.campuslink_android.ui.rental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.domain.repository.RentalRepository

class BorrowedListViewModelFactory(
    private val repository: RentalRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BorrowedListViewModel::class.java)) {
            return BorrowedListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
