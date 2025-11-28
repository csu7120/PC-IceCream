package com.campuslink.backend.domain.review.repository;

import com.campuslink.backend.domain.review.entity.Review;
import com.campuslink.backend.domain.review.entity.ReviewRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // 같은 거래(rentalId)에 대해, 특정 사용자가 이미 리뷰 했는지 체크
    boolean existsByRentalIdAndReviewer_UserId(Integer rentalId, Integer reviewerId);

    // 전체 평균 평점 (역할 구분 X)
    @Query("select avg(r.rating) " +
           "from Review r " +
           "where r.reviewee.userId = :revieweeId")
    Double findAverageRatingByReviewee(@Param("revieweeId") Integer revieweeId);

    // 역할별 평균 평점 (BORROWER / LENDER)
    @Query("select avg(r.rating) " +
           "from Review r " +
           "where r.reviewee.userId = :revieweeId " +
           "and r.roleType = :roleType")
    Double findAverageRatingByRevieweeAndRoleType(
            @Param("revieweeId") Integer revieweeId,
            @Param("roleType") ReviewRoleType roleType
    );

    // 전체 리뷰 개수
    long countByReviewee_UserId(Integer revieweeId);

    // 역할별 리뷰 개수
    long countByReviewee_UserIdAndRoleType(Integer revieweeId, ReviewRoleType roleType);

    // 최근 10개 (요약용)
    List<Review> findTop10ByReviewee_UserIdOrderByCreatedAtDesc(Integer revieweeId);

    // 전체 리스트 (최근순)
    Page<Review> findByReviewee_UserIdOrderByCreatedAtDesc(Integer revieweeId, Pageable pageable);

    // 역할 필터링된 리스트 (최근순)
    Page<Review> findByReviewee_UserIdAndRoleTypeOrderByCreatedAtDesc(
            Integer revieweeId,
            ReviewRoleType roleType,
            Pageable pageable
    );
}
