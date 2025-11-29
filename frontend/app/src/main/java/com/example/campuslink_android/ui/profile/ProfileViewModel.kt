package com.example.campuslink_android.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.UserApi
import com.example.campuslink_android.data.repository.UserRepositoryImpl
import com.example.campuslink_android.domain.model.User
import com.example.campuslink_android.domain.repository.UserRepository
import com.example.campuslink_android.domain.repository.ItemRepository // ⭐️ 추가
import com.example.campuslink_android.domain.repository.RentalRepository // ⭐️ 추가
import com.example.campuslink_android.data.dto.RentalResponseDto // ⭐️ 추가
import com.example.campuslink_android.domain.model.Item // ⭐️ 추가
import kotlinx.coroutines.async // ⭐️ 추가 (병렬 처리를 위해)
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository, // ⭐️ 추가
    private val rentalRepository: RentalRepository // ⭐️ 추가
) : ViewModel() {

    // 1. 내 프로필 정보
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    // 2. 내가 등록한 물품
    private val _myItems = MutableLiveData<List<Item>>()
    val myItems: LiveData<List<Item>> get() = _myItems

    // 3. 내가 빌려준 목록
    private val _myLendings = MutableLiveData<List<RentalResponseDto>>()
    val myLendings: LiveData<List<RentalResponseDto>> get() = _myLendings

    // 4. 내가 빌린 목록
    private val _myRentals = MutableLiveData<List<RentalResponseDto>>()
    val myRentals: LiveData<List<RentalResponseDto>> get() = _myRentals

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // ⭐️ [수정] 모든 정보를 병렬로 로드하는 함수
    fun loadAllProfileData() {
        viewModelScope.launch {
            try {
                // 로그인된 사용자 ID를 ItemRepositoryImpl에서 사용하기 위해 TokenStore에서 가져옵니다.
                // ItemRepositoryImpl.getMyItems()의 파라미터가 있다면 여기에 ID를 넘겨줍니다.
                val userId = TokenStore.getUserId()
                    ?: throw IllegalStateException("로그인된 사용자 ID가 없습니다. 로그아웃 처리 필요")

                // 병렬 API 호출 (async 사용)
                val userDeferred = async { userRepository.getMyInfo() }
                val myItemsDeferred = async { itemRepository.getMyItems(userId) } // 이전 오류 해결 시처럼 userId 전달
                val myLendingsDeferred = async { rentalRepository.getRequestedRentals() } // 내가 빌려준 목록
                val myRentalsDeferred = async { rentalRepository.getMyRentals() } // 내가 빌린 목록

                // 결과 대기 및 LiveData 업데이트
                _user.value = userDeferred.await()
                _myItems.value = myItemsDeferred.await()
                _myLendings.value = myLendingsDeferred.await()
                _myRentals.value = myRentalsDeferred.await()

                _error.value = null

            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "마이페이지 데이터 조회 실패"
            }
        }
    }

    // ⭐️ [수정] Companion object의 create 함수에 Repository 의존성을 추가해야 합니다.
    companion object {
        // 기존 코드에 ItemRepositoryImpl, RentalRepositoryImpl 생성 로직 추가 필요
        fun create(
            userRepository: UserRepository,
            itemRepository: ItemRepository,
            rentalRepository: RentalRepository
        ): ProfileViewModel {
            return ProfileViewModel(userRepository, itemRepository, rentalRepository)
        }
        // DI 프레임워크(Hilt, Koin 등)를 사용한다면 이 코드는 필요하지 않습니다.
    }
}