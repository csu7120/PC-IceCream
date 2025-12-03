package com.example.campuslink_android.ui.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.ItemApi
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.data.repository.ItemRepositoryImpl
import com.example.campuslink_android.data.repository.RentalRepositoryImpl
import com.example.campuslink_android.domain.model.Item
import com.example.campuslink_android.domain.repository.ItemRepository
import com.example.campuslink_android.domain.repository.RentalRepository
import kotlinx.coroutines.launch

class ItemDetailViewModel(
    private val rentalRepository: RentalRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {

    // 🔥 상세 조회 결과
    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item> = _item

    // 🔥 대여 요청 성공 여부
    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    // 🔥 에러 메시지
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // ⭐ 아이템 상세 조회
    fun loadItemDetail(itemId: Int) {
        viewModelScope.launch {
            runCatching {
                itemRepository.getItemDetail(itemId)    // Repository 단에서 서버 요청
            }.onSuccess {
                _item.value = it                       // UI에 전달
            }.onFailure {
                _error.value = it.message ?: "아이템 상세 조회 실패"
            }
        }
    }

    // ⭐ 대여 요청
    fun requestRental(itemId: Int) {
        viewModelScope.launch {
            runCatching {
                rentalRepository.requestRental(itemId)
            }.onSuccess {
                _success.value = true
            }.onFailure {
                _error.value = it.message ?: "대여 요청 실패"
            }
        }
    }

    companion object {
        fun create(): ItemDetailViewModel {

            val rentalRepository: RentalRepository = RentalRepositoryImpl(
                rentalApi = ApiClient.create(RentalApi::class.java),
                tokenStore = TokenStore
            )

            val itemRepository: ItemRepository = ItemRepositoryImpl(
                itemApi = ApiClient.create(ItemApi::class.java),
                tokenStore = TokenStore
            )

            return ItemDetailViewModel(
                rentalRepository = rentalRepository,
                itemRepository = itemRepository
            )
        }
    }
}
