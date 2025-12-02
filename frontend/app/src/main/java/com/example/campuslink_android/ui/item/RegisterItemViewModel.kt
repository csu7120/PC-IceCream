package com.example.campuslink_android.ui.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.domain.repository.ItemRepository
import kotlinx.coroutines.launch
import java.io.File

class RegisterItemViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<Unit>>()
    val registerResult: LiveData<Result<Unit>> = _registerResult

    fun registerItem(
        title: String,
        desc: String?,
        price: Double,
        category: String,
        userId: Int,
        image: File?
    ) {
        viewModelScope.launch {
            runCatching {
                repository.registerItem(
                    title, desc, price, category, userId, image?.let { listOf(it) }
                )
            }.onSuccess {
                _registerResult.value = Result.success(Unit)
            }.onFailure {
                _registerResult.value = Result.failure(it)
            }
        }
    }
}
