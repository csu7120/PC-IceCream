package com.example.campuslink_android.data.dto

import com.google.gson.annotations.SerializedName

// 1. 신고 생성 요청 Body
data class ReportRequestDto(
    @SerializedName("targetUserId") val targetUserId: Int,
    @SerializedName("reason") val reason: String
)

// 2. 신고 상태 변경 요청 Body (관리자용)
data class UpdateReportStatusRequest(
    @SerializedName("status") val status: String // "PENDING", "ACTIONED", "DISMISSED"
)

// 3. 신고 응답 데이터 (Postman의 data 필드 내부)
data class ReportResponseDto(
    @SerializedName("reportId") val reportId: Int,
    @SerializedName("reporterId") val reporterId: Int,
    @SerializedName("reporterName") val reporterName: String,
    @SerializedName("targetUserId") val targetUserId: Int,
    @SerializedName("targetUserName") val targetUserName: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String? // 생성 직후 null일 수 있음
)

// 4. 블랙리스트 응답 데이터
data class BlacklistUserResponse(
    @SerializedName("userId") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("reportCount") val reportCount: Long
)