package com.campuslink.backend.domain.review.entity;

import com.campuslink.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.campuslink.backend.domain.review.entity.ReviewRoleType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                // 한 거래(rentId)에 대해 한 사람이 리뷰는 한 번만 남기도록
                @UniqueConstraint(columnNames = {"rent_id", "reviewer_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;   // 리뷰 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;   // 리뷰 대상자 (평가 받는 사람)

    @Column(name = "rental_id")
    private Integer rentalId;   // 대여 거래라면 여기에 매핑

    @Column(name = "sale_id")
    private Integer saleId;     // 판매 거래라면 여기에 매핑

    @Column(name = "rating", nullable = false)
    private Integer rating;     // tinyint → Integer로 매핑

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, length = 20)
    private ReviewRoleType roleType;
}

