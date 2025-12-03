package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.data.dto.RentalResponseDto

interface RentalRepository {

    /** 물건 대여 요청 */
    suspend fun requestRental(itemId: Int)

    /** 내가 빌려준 물품 중, 대여 '요청' 목록 (REQUESTED 상태만) */
    suspend fun getRequestedRentals(): List<RentalResponseDto>

    /** 내가 빌려준 물품 전체 (REQUESTED 제외 – ACCEPTED, PICKED_UP, RETURNED 등) */
    suspend fun getLentRentals(): List<RentalResponseDto>

    /** 대여 수락 */
    suspend fun acceptRental(rentalId: Int)

    /** 내가 빌린 목록 */
    suspend fun getMyRentals(): List<RentalResponseDto>

    /** 픽업 처리 */
    suspend fun pickupRental(rentalId: Int)

    /** 반납 처리 */
    suspend fun returnRental(rentalId: Int)
}
