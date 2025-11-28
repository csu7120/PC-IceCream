package com.campuslink.backend.domain.review.dto;

import java.util.List;

public record ReviewSummaryResponse(
        Integer userId,

        double borrowerAverageRating,
        long borrowerReviewCount,

        double lenderAverageRating,
        long lenderReviewCount,

        double overallAverageRating,
        long overallReviewCount,

        List<ReviewResponse> recentReviews
) {}
