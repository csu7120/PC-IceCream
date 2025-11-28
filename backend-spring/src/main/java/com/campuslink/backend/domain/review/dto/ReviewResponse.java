package com.campuslink.backend.domain.review.dto;

import com.campuslink.backend.domain.review.entity.Review;
import com.campuslink.backend.domain.review.entity.ReviewRoleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {

    private Integer reviewId;

    private Integer reviewerId;
    private String reviewerName;

    private Integer revieweeId;
    private String revieweeName;

    private Integer rentalId;

    private int rating;
    private String comment;

    private LocalDateTime createdAt;

    /**
     * Review 엔티티 -> ReviewResponse DTO 변환
     */
    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .reviewerId(review.getReviewer() != null ? review.getReviewer().getUserId() : null)
                .reviewerName(review.getReviewer() != null ? review.getReviewer().getName() : null)
                .revieweeId(review.getReviewee() != null ? review.getReviewee().getUserId() : null)
                .revieweeName(review.getReviewee() != null ? review.getReviewee().getName() : null)
                .rentalId(review.getRentalId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}