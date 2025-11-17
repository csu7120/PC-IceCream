package com.campuslink.backend.domain.report.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequest {

    @NotNull
    private Integer targetUserId;   // 신고 대상 유저 ID

    @NotNull
    @Size(min = 1, max = 1000)
    private String reason;       // 신고 사유
}
