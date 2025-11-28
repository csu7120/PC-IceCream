package com.campuslink.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;       
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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

    @Column(name = "rating_avg", precision = 3, scale = 2, nullable = false)
    private BigDecimal ratingAvg = new BigDecimal("0.00");

    @Column(name = "created_at", updatable = false,
            insertable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false,
            columnDefinition = "datetime(6)")
    private LocalDateTime updatedAt;
}
