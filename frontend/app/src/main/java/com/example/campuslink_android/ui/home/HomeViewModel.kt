package com.example.campuslink_android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.ItemApi
import com.example.campuslink_android.data.repository.ItemRepositoryImpl
import com.example.campuslink_android.domain.model.Item
import com.example.campuslink_android.domain.repository.ItemRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadItems() {
        viewModelScope.launch {
            try {
                val result = itemRepository.getItems()
                _items.value = result
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "물품 목록 조회 실패"
            }
        }
    }

    companion object {
        fun create(): HomeViewModel {
            val api = ApiClient.create(ItemApi::class.java)
            val repo: ItemRepository = ItemRepositoryImpl(api, TokenStore)
            return HomeViewModel(repo)
        }
    }
}
