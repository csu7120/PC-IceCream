package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.ApiResponse
import com.example.campuslink_android.data.dto.ItemListResponseDto
import com.example.campuslink_android.data.dto.ItemResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ItemApi {

    // ⭐ 전체 물품 조회
    @GET("/api/items")
    suspend fun getItems(
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("direction") direction: String = "desc"
    ): ApiResponse<ItemListResponseDto>

    // ⭐ 내가 올린 물품 조회
    @GET("/api/items/me")
    suspend fun getMyItems(
        @Query("userId") userId: Int,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<ItemListResponseDto>

    // ⭐ 물품 등록
    @Multipart
    @POST("/api/items")
    suspend fun registerItem(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody?,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part images: List<MultipartBody.Part>? = null,
    ): ApiResponse<ItemResponseDto>

    // --------------------------------------------------
    // ⭐⭐⭐ 추가된 단일 상세 조회 API ⭐⭐⭐
    // --------------------------------------------------
    @GET("/api/items/{id}")
    suspend fun getItemDetail(
        @Path("id") id: Int
    ): ApiResponse<ItemResponseDto>
}
