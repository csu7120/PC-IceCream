package com.campuslink.backend.domain.report.dto;

import com.campuslink.backend.domain.report.entity.Report;
import com.campuslink.backend.domain.report.entity.ReportStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportResponse {

    private Integer reportId;
    private Integer reporterId;
    private String reporterName;
    private Integer targetUserId;
    private String targetUserName;
    private String reason;
    private ReportStatus status;
    private LocalDateTime createdAt;

    public static ReportResponse from(Report report) {
        return ReportResponse.builder()
                .reportId(report.getId())
                .reporterId(report.getReporter().getUserId())
                .reporterName(report.getReporter().getName())
                .targetUserId(report.getTargetUser().getUserId())
                .targetUserName(report.getTargetUser().getName())
                .reason(report.getReason())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
