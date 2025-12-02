package com.example.campuslink_android.data.repository

import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.data.dto.RentalRequestDto
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.domain.repository.RentalRepository

class RentalRepositoryImpl(
    private val api: RentalApi,
    private val tokenStore: TokenStore
) : RentalRepository {

    // 내가 빌린 목록
    override suspend fun getMyRentals(): List<RentalResponseDto> {
        val email = tokenStore.getEmail() ?: return emptyList()
        val response = api.getMyRentals(email)
        return response.body()?.data ?: emptyList()
    }

    // 내가 빌려준 목록
    override suspend fun getMyLendings(): List<RentalResponseDto> {
        val email = tokenStore.getEmail() ?: return emptyList()
        val response = api.getMyLendings(email)
        return response.body()?.data ?: emptyList()
    }

    // 대여 요청 목록(REQUESTED 상태만 필터링)
    override suspend fun getRequestedRentals(): List<RentalResponseDto> {
        val email = tokenStore.getEmail() ?: return emptyList()
        val response = api.getMyLendings(email)
        val list = response.body()?.data ?: emptyList()
        return list.filter { it.status == "REQUESTED" }
    }

    // 대여 요청 승인
    override suspend fun acceptRental(rentalId: Int) {
        val email = tokenStore.getEmail()
            ?: throw IllegalStateException("로그인이 필요합니다.")

        api.acceptRental(
            id = rentalId,
            email = email
        )
    }

    // 대여 요청 보내기
    override suspend fun requestRental(itemId: Int) {
        val email = tokenStore.getEmail()
            ?: throw IllegalStateException("로그인이 필요합니다.")

        val now = java.time.LocalDateTime.now().toString()
        val end = java.time.LocalDateTime.now().plusDays(2).toString()

        val body = RentalRequestDto(
            itemId = itemId,
            startAt = now,
            endAt = end
        )

        val response = api.requestRental(
            email = email,
            body = body
        )

        if (!response.isSuccessful) {
            throw IllegalStateException("대여 요청 실패")
        }
    }
}
