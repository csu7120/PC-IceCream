package com.campuslink.backend.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlacklistUserResponse {
    private Integer userId;
    private String name;
    private long reportCount;
}
