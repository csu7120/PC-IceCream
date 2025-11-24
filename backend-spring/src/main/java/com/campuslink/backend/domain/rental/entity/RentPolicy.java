package com.campuslink.backend.domain.rental.entity;

import com.campuslink.backend.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rent_policies")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer policyId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false, unique = true)
    private Item item;

    private Integer minDays;
    private Integer maxDays;

    @Column(name = "default_daily_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal defaultDailyPrice;

    @Column(name = "default_deposit", precision = 10, scale = 2)
    private BigDecimal defaultDeposit;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (minDays == null) minDays = 1;
        if (maxDays == null) maxDays = 14;
        if (defaultDeposit == null) defaultDeposit = BigDecimal.ZERO;
    }
}
