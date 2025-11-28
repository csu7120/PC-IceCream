package com.campuslink.backend.domain.review.dto;

import com.campuslink.backend.domain.review.entity.ReviewRoleType;
import com.campuslink.backend.domain.review.entity.ReviewTag;
import jakarta.validation.constraints.*;

import java.util.Set;

public record ReviewCreateRequest(
        @NotNull Integer rentId,
        @NotNull Integer targetUserId,
        @NotNull ReviewRoleType roleType,
        @Min(1) @Max(5) int rating,
        @Size(max = 500) String comment,
        Set<ReviewTag> tags
) {}
