package com.example.campuslink_android.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.domain.repository.ItemRepository

class RegisterItemViewModelFactory(
    private val repository: ItemRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterItemViewModel(repository) as T
    }
}
