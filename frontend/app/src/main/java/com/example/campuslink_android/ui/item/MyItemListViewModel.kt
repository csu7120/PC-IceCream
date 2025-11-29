package com.example.campuslink_android.ui.item

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

class MyItemListViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    /**
     * 내가 올린 물품 목록 조회
     */
    fun loadMyItems() {
        viewModelScope.launch {
            try {
                // ✅ TokenStore 에 저장되어 있는 내 userId 사용
                val userId = TokenStore.getUserId()
                    ?: throw IllegalStateException("로그인 정보가 없습니다.")

                // ✅ 내 물품만 조회
                val list = itemRepository.getMyItems(userId)

                _items.value = list
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "내 물품 조회 실패"
            }
        }
    }

    companion object {
        fun create(): MyItemListViewModel {
            val api = ApiClient.create(ItemApi::class.java)
            val repo: ItemRepository = ItemRepositoryImpl(api, TokenStore)
            return MyItemListViewModel(repo)
        }
    }
}
