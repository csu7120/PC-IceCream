package com.campuslink.backend.domain.review.service;

import com.campuslink.backend.domain.review.dto.ReviewCreateRequest;
import com.campuslink.backend.domain.review.dto.ReviewResponse;
import com.campuslink.backend.domain.review.dto.ReviewSummaryResponse;
import com.campuslink.backend.domain.review.entity.Review;
import com.campuslink.backend.domain.review.entity.ReviewRoleType;
import com.campuslink.backend.domain.review.repository.ReviewRepository;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviews;
    private final UserRepository users;
    // private final RentRepository rents; // 실제 대여 엔티티 있으면 여기에 주입해서 상태 체크

    /**
     * 리뷰 생성 (거래 완료 후)
     */
    public ReviewResponse createReview(String reviewerEmail, ReviewCreateRequest req) {

        // 1) 리뷰 작성자 조회 (로그인 유저)
        User reviewer = users.findByEmail(reviewerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        // 2) 자기 자신에게 리뷰 방지
        if (reviewer.getUserId().equals(req.targetUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "자기 자신에게는 리뷰를 남길 수 없습니다."
            );
        }

        // 3) 리뷰 대상 사용자 조회
        User target = users.findById(req.targetUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "대상 사용자가 존재하지 않습니다."
                ));

        // 4) 역할 필수 체크 (BORROWER / LENDER)
        if (req.roleType() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "역할 정보가 필요합니다. (BORROWER / LENDER)"
            );
        }

        // 5) 같은 거래(rentId)에 대해 이 사용자가 이미 리뷰를 작성했는지 확인
        if (req.rentId() != null &&
                reviews.existsByRentalIdAndReviewer_UserId(req.rentId(), reviewer.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "해당 거래에 대한 리뷰는 이미 작성되었습니다."
            );
        }

        // 6) 별점 범위 체크
        int rating = req.rating();
        if (rating < 1 || rating > 5) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "별점은 1~5 사이여야 합니다."
            );
        }

        // TODO: 여기서 rent 상태/기간 체크 (완료 상태인지, 7일 이내인지 등)
        // Rent rent = rents.findById(req.rentId())...
        // if (!rent.isCompleted()) throw ...

        // 7) Review 엔티티 생성
        Review review = Review.builder()
                .reviewer(reviewer)              // 작성자
                .reviewee(target)                // 리뷰 대상자
                .rentalId(req.rentId())          // 대여 거래 ID (없으면 null 가능)
                .roleType(req.roleType())        // BORROWER / LENDER
                .rating(rating)
                .comment(req.comment())
                .build();

        // 8) 저장
        Review saved = reviews.save(review);

        // 9) 대상 유저의 평균 평점 갱신 (users.rating_avg 업데이트)
        updateUserRatingAvg(target.getUserId());

        // 10) DTO로 변환 후 반환
        return ReviewResponse.from(saved);
    }

    /**
     * 대상 유저의 평균 평점을 다시 계산해서 users.rating_avg 에 반영
     */
    private void updateUserRatingAvg(Integer userId) {
        Double avg = reviews.findAverageRatingByReviewee(userId);

        User user = users.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "사용자가 존재하지 않습니다."
                ));

        double value = (avg != null) ? avg : 0.0;

        // BigDecimal 로 소수 둘째자리까지 반올림
        user.setRatingAvg(
                BigDecimal.valueOf(value)
                        .setScale(2, RoundingMode.HALF_UP)
        );

        users.save(user);
    }

    /**
     * 특정 유저에 대한 리뷰 요약 (프로필용)
     * - 역할별 평균 평점 및 개수
     * - 전체 평균 평점 및 개수
     * - 최근 리뷰 10개
     */
    public ReviewSummaryResponse getUserReviewSummary(Integer userId) {

        // 1) 유저 존재 여부 확인
        User user = users.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "사용자가 존재하지 않습니다."
                ));

        // 2) 역할별 평균 / 개수 조회 (reviewee = 평가받는 사람)
        Double avgBorrower = reviews.findAverageRatingByRevieweeAndRoleType(
                userId, ReviewRoleType.BORROWER);
        Double avgLender = reviews.findAverageRatingByRevieweeAndRoleType(
                userId, ReviewRoleType.LENDER);
        Double avgOverall = reviews.findAverageRatingByReviewee(userId);

        long cntBorrower = reviews.countByReviewee_UserIdAndRoleType(
                userId, ReviewRoleType.BORROWER);
        long cntLender = reviews.countByReviewee_UserIdAndRoleType(
                userId, ReviewRoleType.LENDER);
        long cntOverall = reviews.countByReviewee_UserId(userId);

        double safeBorrowerAvg = (avgBorrower != null) ? avgBorrower : 0.0;
        double safeLenderAvg   = (avgLender   != null) ? avgLender   : 0.0;
        double safeOverallAvg  = (avgOverall  != null) ? avgOverall  : 0.0;

        // 3) 최근 리뷰 10개 조회
        List<ReviewResponse> recent = reviews
                .findByReviewee_UserIdOrderByCreatedAtDesc(
                        userId, PageRequest.of(0, 10))
                .stream()
                .map(ReviewResponse::from)
                .toList();

        // 4) 요약 DTO 반환
        return new ReviewSummaryResponse(
                user.getUserId(),
                safeBorrowerAvg,
                cntBorrower,
                safeLenderAvg,
                cntLender,
                safeOverallAvg,
                cntOverall,
                recent
        );
    }

    /**
     * 유저 리뷰 리스트 조회
     * - role 이 null 이면: 해당 유저가 받은 모든 리뷰
     * - role 이 BORROWER/LENDER 면: 해당 역할로 받은 리뷰만
     */
    public Page<ReviewResponse> getUserReviews(Integer userId,
                                               ReviewRoleType role,
                                               Pageable pageable) {

        // 1) 유저 존재 여부 확인
        if (!users.existsById(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "사용자가 존재하지 않습니다."
            );
        }

        Page<Review> page;

        // 2) 역할 필터 여부에 따라 분기
        if (role == null) {
            page = reviews.findByReviewee_UserIdOrderByCreatedAtDesc(userId, pageable);
        } else {
            page = reviews.findByReviewee_UserIdAndRoleTypeOrderByCreatedAtDesc(
                    userId, role, pageable);
        }

        // 3) DTO 변환
        return page.map(ReviewResponse::from);
    }
}
