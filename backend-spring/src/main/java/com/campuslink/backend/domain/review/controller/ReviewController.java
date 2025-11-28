package com.campuslink.backend.domain.review.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.review.dto.ReviewCreateRequest;
import com.campuslink.backend.domain.review.dto.ReviewResponse;
import com.campuslink.backend.domain.review.dto.ReviewSummaryResponse;
import com.campuslink.backend.domain.review.entity.ReviewRoleType;
import com.campuslink.backend.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /** 리뷰 생성 */
    @PostMapping("/reviews")
    public ApiResponse<ReviewResponse> createReview(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody ReviewCreateRequest request
    ) {
        ReviewResponse response = reviewService.createReview(email, request);
        return ApiResponse.ok(response);
    }

    /** ✅ 유저 리뷰 요약: /api/users/{userId}/reviews/summary */
    @GetMapping("/users/{userId}/reviews/summary")
    public ApiResponse<ReviewSummaryResponse> getUserReviewSummary(
            @PathVariable Integer userId
    ) {
        ReviewSummaryResponse summary = reviewService.getUserReviewSummary(userId);
        return ApiResponse.ok(summary);
    }

    /** ✅ 유저 리뷰 리스트: /api/users/{userId}/reviews */
    @GetMapping("/users/{userId}/reviews")
    public ApiResponse<Page<ReviewResponse>> getUserReviews(
            @PathVariable Integer userId,
            @RequestParam(required = false) ReviewRoleType role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> result = reviewService.getUserReviews(userId, role, pageable);
        return ApiResponse.ok(result);
    }
}
