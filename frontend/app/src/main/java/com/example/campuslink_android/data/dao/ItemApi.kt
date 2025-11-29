package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.ItemListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ItemApi {

    @GET("/api/items")
    suspend fun getItems(
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): ItemListResponseDto
    @GET("/api/items/me")
    suspend fun getMyItems(
        @Query("userId") userId: Int,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ItemListResponseDto
}
