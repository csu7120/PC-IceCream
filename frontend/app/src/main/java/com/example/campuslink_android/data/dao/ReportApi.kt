package com.example.campuslink_android.data.dao

import com.example.campuslink_android.data.dto.ApiResponse
import com.example.campuslink_android.data.dto.BlacklistUserResponse
import com.example.campuslink_android.data.dto.ReportRequestDto
import com.example.campuslink_android.data.dto.ReportResponseDto
import com.example.campuslink_android.data.dto.UpdateReportStatusRequest
import retrofit2.Response
import retrofit2.http.*

interface ReportApi {

    // 신고하기
    // Postman: POST /api/reports?reporterId=7
    @POST("api/reports")
    suspend fun createReport(
        @Query("reporterId") reporterId: Int,
        @Body request: ReportRequestDto
    ): Response<ApiResponse<ReportResponseDto>>

    // 내 신고 내역 조회
    // Postman: GET /api/reports/me?reporterId=7
    @GET("api/reports/me")
    suspend fun getMyReports(
        @Query("reporterId") reporterId: Int
    ): Response<ApiResponse<List<ReportResponseDto>>>

    // (관리자) 특정 유저가 받은 신고 조회
    @GET("api/reports/target/{userId}")
    suspend fun getReportsForTarget(
        @Path("userId") userId: Int
    ): Response<ApiResponse<List<ReportResponseDto>>>

    // (관리자) 신고 상태 변경
    @PATCH("api/reports/{reportId}/status")
    suspend fun updateReportStatus(
        @Path("reportId") reportId: Int,
        @Body body: UpdateReportStatusRequest
    ): Response<ApiResponse<ReportResponseDto>>

    // (관리자) 블랙리스트 조회
    @GET("api/reports/blacklist")
    suspend fun getBlacklist(
        @Query("threshold") threshold: Long = 3
    ): Response<ApiResponse<List<BlacklistUserResponse>>>
}