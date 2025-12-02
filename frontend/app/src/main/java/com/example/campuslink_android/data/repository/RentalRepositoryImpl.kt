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
     * ⭐ 내가 빌려준 목록 (대여 요청 들어온 리스트)
     */
    override suspend fun getRequestedRentals(): List<RentalResponseDto> {
        val email = tokenStore.getEmail()
            ?: throw IllegalStateException("로그인이 필요합니다.")

        val response = rentalApi.getMyLendings(email)

        if (!response.isSuccessful) {
            throw IllegalStateException("요청 목록 불러오기 실패: ${response.code()}")
        }

        // ⭐ REQUESTED만 남김
        return response.body()?.data
            ?.filter { it.status == "REQUESTED" }
            ?: emptyList()
    }


    /**
     * ⭐ 대여 수락 (중요!)
     * email은 ViewModel에서 받아서 전달받도록 구조 개선됨
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
}
