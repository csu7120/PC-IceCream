package com.example.campuslink_android.data.repository

import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.ItemApi
import com.example.campuslink_android.data.mapper.toDomain
import com.example.campuslink_android.domain.model.Item
import com.example.campuslink_android.domain.repository.ItemRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ItemRepositoryImpl(
    private val itemApi: ItemApi,
    private val tokenStore: TokenStore
) : ItemRepository {

    // 전체 물품
    override suspend fun getItems(): List<Item> {
        val response = itemApi.getItems()

        if (!response.success) {
            throw IllegalStateException(response.message ?: "물품 목록 조회 실패")
        }

        val pageDto = response.data
            ?: throw IllegalStateException("서버에서 물품 데이터를 받지 못했습니다.")

        val content = pageDto.content ?: emptyList()
        return content.map { it.toDomain() }
    }


    // 내가 올린 물품
    override suspend fun getMyItems(userId: Int): List<Item> {
        val response = itemApi.getMyItems(userId)

        if (!response.success) {
            throw IllegalStateException(response.message ?: "내 물품 목록 조회 실패")
        }

        val pageDto = response.data
            ?: throw IllegalStateException("서버에서 내 물품 데이터를 받지 못했습니다.")

        val content = pageDto.content ?: emptyList()
        return content.map { it.toDomain() }
    }


    // 물품 등록
    override suspend fun registerItem(
        title: String,
        description: String?,
        price: Double,
        category: String,
        userId: Int,
        images: List<File>?
    ): Item {

        val titleRb = title.toRequestBody("text/plain".toMediaType())
        val descRb = description?.toRequestBody("text/plain".toMediaType())
        val priceRb = price.toString().toRequestBody("text/plain".toMediaType())
        val categoryRb = category.toRequestBody("text/plain".toMediaType())
        val userIdRb = userId.toString().toRequestBody("text/plain".toMediaType())

        val imageParts = images?.mapIndexed { index, file ->
            val reqFile = file.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData(
                "images",
                "image_$index.jpg",
                reqFile
            )
        }

        val response = itemApi.registerItem(
            title = titleRb,
            description = descRb,
            price = priceRb,
            category = categoryRb,
            userId = userIdRb,
            images = imageParts
        )

        // ★ 등록 API는 success/message 가 없음 → 바로 domain 변환
        return response.toDomain()
    }
}
