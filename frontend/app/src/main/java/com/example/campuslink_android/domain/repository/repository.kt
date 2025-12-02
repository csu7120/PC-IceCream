package com.example.campuslink_android.domain.repository

import com.example.campuslink_android.data.dto.ReportResponseDto
import com.example.campuslink_android.data.dto.BlacklistUserResponse

interface ReportRepository {
    suspend fun createReport(reporterId: Int, targetUserId: Int, reason: String): Result<ReportResponseDto>
    suspend fun getMyReports(reporterId: Int): Result<List<ReportResponseDto>>
    suspend fun getBlacklist(threshold: Long): Result<List<BlacklistUserResponse>>
}