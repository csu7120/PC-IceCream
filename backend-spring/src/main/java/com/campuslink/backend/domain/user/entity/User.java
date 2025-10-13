// entity/User.java
package com.campuslink.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;        // ⬅ 추가
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")              // DB 컬럼명 매핑
    private Integer userId;

    @Column(name = "campus_id", nullable = false)
    private Integer campusId;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String phone;

    @Column(name = "profile_url", length = 255)
    private String profileUrl;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    // ⬇ Double → BigDecimal 로 변경 (DECIMAL(3,2)와 1:1)
    @Column(name = "rating_avg", precision = 3, scale = 2, nullable = false)
    private BigDecimal ratingAvg = new BigDecimal("0.00");

    @Column(name = "created_at", updatable = false,
            insertable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false,
            columnDefinition = "datetime(6)")
    private LocalDateTime updatedAt;
}
