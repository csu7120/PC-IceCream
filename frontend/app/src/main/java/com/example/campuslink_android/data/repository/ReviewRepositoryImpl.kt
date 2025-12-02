package com.example.campuslink_android.data.repository

import android.util.Log
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.ReviewApi
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.data.dto.ReviewCreateRequest
import com.example.campuslink_android.domain.repository.ReviewRepository

class ReviewRepositoryImpl(
    private val api: ReviewApi,
    private val tokenStore: TokenStore
) : ReviewRepository {

    override suspend fun createReview(
        rental: RentalResponseDto,
        rating: Double,
        comment: String,
        roleType: String
    ) {
        val token = tokenStore.getToken() ?: throw IllegalStateException("로그인 필요")
        val bearer = "Bearer $token"

        val currentUserId = tokenStore.getUserId()
            ?: throw IllegalStateException("로그인 사용자 ID 없음")


        // borrowerId = 빌린 사람
        // lenderId = 빌려준 사람
        val targetUserId =
            if (currentUserId == rental.renterId) rental.lenderId
            else rental.renterId

        val body = ReviewCreateRequest(
            rentId = rental.rentalId,
            targetUserId = targetUserId,
            roleType = roleType,
            rating = rating.toInt(),
            comment = comment,
            tags = null
        )

        val res = api.createReview(bearer, body)

        if (!res.isSuccessful)
            throw IllegalStateException("리뷰 작성 실패: ${res.code()}")
        Log.d("ReviewRepo", "서버로 전송중: rentId=${body.rentId}, rating=${body.rating}")

    }
}
