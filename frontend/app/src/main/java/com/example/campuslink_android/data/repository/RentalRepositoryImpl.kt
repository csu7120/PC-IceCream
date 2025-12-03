package com.example.campuslink_android.data.repository

import android.util.Log
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.data.dto.RentalRequestDto
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.RentalRepository

class RentalRepositoryImpl(
    private val rentalApi: RentalApi,
    private val tokenStore: TokenStore
) : RentalRepository {

    /**
     * ⭐ 물건 대여 요청
     */
    override suspend fun requestRental(itemId: Int) {
        val email = tokenStore.getEmail()
            ?: throw IllegalStateException("로그인 이메일 없음")

        val body = RentalRequestDto(
            itemId = itemId,
            startAt = "2025-11-24T12:00:00",
            endAt = "2025-11-26T12:00:00"
        )

        val response = rentalApi.requestRental(email, body)

        if (!response.isSuccessful) {
            throw IllegalStateException("대여 요청 실패: ${response.code()}")
        }
    }

    /**
     * ⭐ 내가 빌려준 목록 중 '요청 상태(REQUESTED)'만
     */
    override suspend fun getRequestedRentals(): List<RentalResponseDto> {
        val email = tokenStore.getEmail()
            ?: throw IllegalStateException("로그인이 필요합니다.")

        val response = rentalApi.getMyLendings(email)

        if (!response.isSuccessful) {
            throw IllegalStateException("요청 목록 불러오기 실패: ${response.code()}")
        }

        return response.body()?.data
            ?.filter { it.status == "REQUESTED" }
            ?: emptyList()
    }

    /**
     * ⭐ 내가 빌려준 목록 전체 (REQUESTED 제외)
     * → '내가 빌려준 목록' 화면에서 사용할 데이터
     */
    override suspend fun getLentRentals(): List<RentalResponseDto> {
        val email = tokenStore.getEmail()
            ?: throw IllegalStateException("로그인이 필요합니다.")

        val response = rentalApi.getMyLendings(email)

        if (!response.isSuccessful) {
            throw IllegalStateException("내가 빌려준 목록 불러오기 실패: ${response.code()}")
        }

        return response.body()?.data
            // 요청 대기건은 '대여 요청 목록'에서만 보이도록 제외
            ?.filter { it.status != "REQUESTED" }
            ?: emptyList()
    }

    /**
     * ⭐ 대여 수락
     */
    override suspend fun acceptRental(rentalId: Int) {
        val token = tokenStore.getToken()
            ?: throw IllegalStateException("로그인 토큰 없음")

        val bearer = "Bearer $token"

        Log.e("REPO_ACCEPT", "Repository called with rentalId=$rentalId")

        val response = rentalApi.acceptRental(bearer, rentalId)

        Log.e("REPO_ACCEPT", "Response code=${response.code()}")

        if (!response.isSuccessful) {
            throw IllegalStateException("대여 수락 실패: ${response.code()}")
        }
    }

    /**
     * ⭐ 내가 빌린 목록
     */
    override suspend fun getMyRentals(): List<RentalResponseDto> {
        val email = tokenStore.getEmail() ?: return emptyList()

        return try {
            val response = rentalApi.getMyRentals(email)

            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else {
                Log.e("RentalRepository", "getMyRentals() failed: code=${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RentalRepository", "getMyRentals() exception", e)
            emptyList()
        }
    }

    override suspend fun pickupRental(rentalId: Int) {
        val email = tokenStore.getEmail() ?: error("로그인 필요")
        val res = rentalApi.pickupRental(rentalId, email)
        if (!res.isSuccessful) throw IllegalStateException("픽업 실패")
    }

    override suspend fun returnRental(rentalId: Int) {
        val email = tokenStore.getEmail() ?: error("로그인 필요")
        val res = rentalApi.returnRental(rentalId, email)
        if (!res.isSuccessful) throw IllegalStateException("반납 실패")
    }
}
