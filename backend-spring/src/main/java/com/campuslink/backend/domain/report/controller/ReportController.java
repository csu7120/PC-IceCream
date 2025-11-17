package com.campuslink.backend.domain.report.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.report.dto.BlacklistUserResponse;
import com.campuslink.backend.domain.report.dto.ReportRequest;
import com.campuslink.backend.domain.report.dto.ReportResponse;
import com.campuslink.backend.domain.report.dto.UpdateReportStatusRequest;
import com.campuslink.backend.domain.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

   
    @PostMapping
    public ApiResponse<ReportResponse> createReport(
            @RequestParam Integer reporterId,         
            @RequestBody @Valid ReportRequest request
    ) {
        ReportResponse response = reportService.createReport(reporterId, request);
        return ApiResponse.ok(response);
    }

   
    @GetMapping("/me")
    public ApiResponse<List<ReportResponse>> getMyReports(
            @RequestParam Integer reporterId           
    ) {
        List<ReportResponse> responses = reportService.getMyReports(reporterId);
        return ApiResponse.ok(responses);
    }

   
    @GetMapping("/target/{userId}")
    public ApiResponse<List<ReportResponse>> getReportsForTarget(
            @PathVariable Integer userId              
    ) {
        List<ReportResponse> responses = reportService.getReportsForTarget(userId);
        return ApiResponse.ok(responses);
    }

   
    @PatchMapping("/{reportId}/status")
    public ApiResponse<ReportResponse> updateStatus(
            @PathVariable Integer reportId,
            @RequestBody @Valid UpdateReportStatusRequest request
    ) {
        ReportResponse response = reportService.updateStatus(reportId, request);
        return ApiResponse.ok(response);
    }

   
    @GetMapping("/blacklist")
    public ApiResponse<List<BlacklistUserResponse>> getBlacklist(
            @RequestParam(defaultValue = "3") long threshold
    ) {
        List<BlacklistUserResponse> responses = reportService.getBlacklist(threshold);
        return ApiResponse.ok(responses);
    }
}
