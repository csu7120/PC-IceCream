package com.example.campuslink_android.data.repository

import com.example.campuslink_android.data.dao.ReportApi
import com.example.campuslink_android.data.dto.ReportRequestDto
import com.example.campuslink_android.data.dto.ReportResponseDto
import com.example.campuslink_android.data.dto.BlacklistUserResponse
import com.example.campuslink_android.domain.repository.ReportRepository

class ReportRepositoryImpl(private val api: ReportApi) : ReportRepository {

    override suspend fun createReport(reporterId: Int, targetUserId: Int, reason: String): Result<ReportResponseDto> {
        return try {
            val response = api.createReport(reporterId, ReportRequestDto(targetUserId, reason))
            if (response.isSuccessful && response.body()?.success == true) {
                // response.body()?.data가 null이 아니라고 가정
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "신고 요청 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyReports(reporterId: Int): Result<List<ReportResponseDto>> {
        return try {
            val response = api.getMyReports(reporterId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "내역 조회 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBlacklist(threshold: Long): Result<List<BlacklistUserResponse>> {
        return try {
            val response = api.getBlacklist(threshold)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception("블랙리스트 조회 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}