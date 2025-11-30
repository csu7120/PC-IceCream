package com.example.campuslink_android.ui.item.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.domain.model.Item
import com.example.campuslink_android.domain.repository.ItemRepository
import kotlinx.coroutines.launch
import java.io.File

class ItemRegisterViewModel(
        private val itemRepository: ItemRepository
) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String?>()

    fun registerItem(
            title: String,
            description: String?,
            price: Double,
            category: String,
            userId: Int,
            images: List<File>?
    ) {
        viewModelScope.launch {
            try {
                loading.value = true

                val item: Item = itemRepository.registerItem(
                        title = title,
                        description = description,
                        price = price,
                        category = category,
                        userId = userId,
                        images = images
                )

                loading.value = false
                success.value = true

            } catch (e: Exception) {
                loading.value = false
                success.value = false
                errorMessage.value = e.message
            }
        }
    }
}
