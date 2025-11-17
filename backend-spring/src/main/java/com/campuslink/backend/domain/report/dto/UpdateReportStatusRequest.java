package com.campuslink.backend.domain.report.dto;

import com.campuslink.backend.domain.report.entity.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReportStatusRequest {

    @NotNull
    private ReportStatus status;
}
