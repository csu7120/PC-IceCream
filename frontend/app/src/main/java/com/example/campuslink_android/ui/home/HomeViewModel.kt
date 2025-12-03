package com.example.campuslink_android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.ItemApi
import com.example.campuslink_android.data.dao.NotificationApi
import com.example.campuslink_android.data.repository.ItemRepositoryImpl
import com.example.campuslink_android.data.repository.NotificationRepositoryImpl
import com.example.campuslink_android.domain.model.Item
import com.example.campuslink_android.domain.repository.ItemRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val itemRepository: ItemRepository,
    private val notificationRepository: NotificationRepositoryImpl
) : ViewModel() {

    // ---------------------
    // 기존 아이템 목록 기능
    // ---------------------
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadItems() {
        viewModelScope.launch {
            try {
                val result = itemRepository.getItems()

                // ⭐ [수정됨] 받아온 리스트를 최신순(ID가 큰 순서)으로 정렬합니다.
                // 이렇게 하면 새로 등록한 물품이 맨 위로 올라옵니다.
                // (만약 Item 클래스에 'id' 대신 'itemId'라고 되어 있다면 it.itemId로 수정해주세요!)
                val sortedList = result.sortedByDescending { it.id }

                _items.value = sortedList
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "물품 목록 조회 실패"
            }
        }
    }

    // ---------------------
    // ⭐ 알림 개수 기능 추가
    // ---------------------
    private val _unread = MutableLiveData<Int>()
    val unread: LiveData<Int> get() = _unread

    fun loadUnreadCount() {
        viewModelScope.launch {
            try {
                val count = notificationRepository.getUnreadCount()
                _unread.value = count
            } catch (e: Exception) {
                _unread.value = 0
            }
        }
    }

    companion object {
        fun create(): HomeViewModel {

            // Item Repository (기존 그대로)
            val itemApi = ApiClient.create(ItemApi::class.java)
            val itemRepo: ItemRepository = ItemRepositoryImpl(itemApi, TokenStore)

            // Notification Repository 추가
            val notiApi = ApiClient.create(NotificationApi::class.java)
            val notiRepo = NotificationRepositoryImpl(notiApi, TokenStore)

            return HomeViewModel(itemRepo, notiRepo)
        }
    }
}