package com.campuslink.backend.domain.rental.entity;

import com.campuslink.backend.domain.item.entity.Item;
import com.campuslink.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rentalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender; // 빌려주는 사람(아이템 owner)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "daily_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyPrice;

    @Column(name = "deposit", precision = 10, scale = 2)
    private BigDecimal deposit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    private LocalDateTime pickedUpAt;
    private LocalDateTime returnedAt;

    @Column(name = "late_fee", precision = 10, scale = 2)
    private BigDecimal lateFee;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = RentalStatus.REQUESTED;
        if (deposit == null) deposit = BigDecimal.ZERO;
        if (lateFee == null) lateFee = BigDecimal.ZERO;
    }

    public long getDays() {
        return java.time.Duration.between(startAt, endAt).toDays();
    }
}
