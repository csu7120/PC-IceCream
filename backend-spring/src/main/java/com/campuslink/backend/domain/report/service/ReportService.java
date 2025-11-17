package com.campuslink.backend.domain.report.service;

import com.campuslink.backend.domain.report.dto.BlacklistUserResponse;
import com.campuslink.backend.domain.report.dto.ReportRequest;
import com.campuslink.backend.domain.report.dto.ReportResponse;
import com.campuslink.backend.domain.report.dto.UpdateReportStatusRequest;
import com.campuslink.backend.domain.report.entity.Report;
import com.campuslink.backend.domain.report.entity.ReportStatus;
import com.campuslink.backend.domain.report.repository.ReportRepository;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportResponse createReport(Integer reporterId, ReportRequest request) {
        if (reporterId.equals(request.getTargetUserId())) {
            throw new IllegalArgumentException("자기 자신은 신고할 수 없습니다.");
        }

        User reporter = userRepository.findById(reporterId)  
                .orElseThrow(() -> new EntityNotFoundException("신고자를 찾을 수 없습니다."));

        User target = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new EntityNotFoundException("신고 대상 사용자를 찾을 수 없습니다."));

        Report report = Report.builder()
                .reporter(reporter)
                .targetUser(target)
                .reason(request.getReason())
                .status(ReportStatus.PENDING)
                .build();

        Report saved = reportRepository.save(report);
        return ReportResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getMyReports(Integer reporterId) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        return reportRepository.findByReporter(reporter).stream()
                .map(ReportResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getReportsForTarget(Integer targetUserId) {
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        return reportRepository.findByTargetUser(target).stream()
                .map(ReportResponse::from)
                .toList();
    }

    public ReportResponse updateStatus(Integer reportId, UpdateReportStatusRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("신고 내역을 찾을 수 없습니다."));

        report.setStatus(request.getStatus());
        return ReportResponse.from(report);
    }

    @Transactional(readOnly = true)
    public List<BlacklistUserResponse> getBlacklist(long threshold) {
        return reportRepository.findBlacklistedUsers(
                List.of(ReportStatus.ACTIONED),
                threshold
        );
    }
}