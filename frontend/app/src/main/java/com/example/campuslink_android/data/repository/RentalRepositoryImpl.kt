package com.example.campuslink_android.data.repository

import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.data.dto.RentalRequestDto
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.RentalRepository

class RentalRepositoryImpl(
    private val rentalApi: RentalApi
) : RentalRepository {

    override suspend fun requestRental(itemId: Int) {
        val email = TokenStore.getEmail()
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

    override suspend fun getRequestedRentals(): List<RentalResponseDto> {
        val email = TokenStore.getEmail()
            ?: throw IllegalStateException("로그인 필요")

        val response = rentalApi.getMyLendings(email)
        if (!response.isSuccessful) {
            throw IllegalStateException("요청 목록 불러오기 실패: ${response.code()}")
        }

        return response.body()?.data ?: emptyList()
    }

    override suspend fun acceptRental(rentalId: Int) {
        val email = TokenStore.getEmail()
            ?: throw IllegalStateException("로그인 이메일 없음")

        val response = rentalApi.acceptRental(rentalId, email)
        if (!response.isSuccessful) {
            throw IllegalStateException("대여 수락 실패: ${response.code()}")
        }
    }
}
