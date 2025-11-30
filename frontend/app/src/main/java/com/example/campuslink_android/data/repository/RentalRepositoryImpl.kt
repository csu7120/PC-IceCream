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
     * 물건 대여 요청
     */
    override suspend fun requestRental(itemId: Int) {
        val email = tokenStore.getEmail()
            ?: throw IllegalStateException("로그인 이메일 없음")

        // 👉 TODO: start/end 날짜는 나중에 UI에서 받도록 변경 가능
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
     * 내가 빌려준 목록 (내 물건에 들어온 대여 요청들)
     */
    override suspend fun getRequestedRentals(): List<RentalResponseDto> {
        val email = tokenStore.getEmail()
            ?: throw IllegalStateException("로그인이 필요합니다.")

        val response = rentalApi.getMyLendings(email)
        if (!response.isSuccessful) {
            throw IllegalStateException("요청 목록 불러오기 실패: ${response.code()}")
        }

        return response.body()?.data ?: emptyList()
    }


    /**
     * 대여 수락
     */
    override suspend fun acceptRental(rentalId: Int) {
        val email = tokenStore.getEmail()
            ?: throw IllegalStateException("로그인 이메일 없음")

        val response = rentalApi.acceptRental(rentalId, email)
        if (!response.isSuccessful) {
            throw IllegalStateException("대여 수락 실패: ${response.code()}")
        }
    }

    override suspend fun getMyRentals(): List<RentalResponseDto> {
        Log.d("RentalRepository", "getRequestedRentals email=${tokenStore.getEmail()}")
        Log.d("RentalRepository", "getMyRentals email=${tokenStore.getEmail()}")

        val email = tokenStore.getEmail() ?: return emptyList()

        return try {
            val response = rentalApi.getMyRentals(email)

            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else {
                // 404 등 에러 나도 앱은 안 죽게
                android.util.Log.e(
                    "RentalRepository",
                    "getMyRentals() failed: code=${response.code()}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            android.util.Log.e("RentalRepository", "getMyRentals() exception", e)
            emptyList()
        }
    }
}