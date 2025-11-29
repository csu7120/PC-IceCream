package com.example.campuslink_android.data.repository

import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.ItemApi
import com.example.campuslink_android.data.mapper.toDomain
import com.example.campuslink_android.domain.model.Item
import com.example.campuslink_android.domain.repository.ItemRepository

class ItemRepositoryImpl(
    private val itemApi: ItemApi,
    private val tokenStore: TokenStore
) : ItemRepository {

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
}
