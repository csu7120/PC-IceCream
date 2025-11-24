package com.campuslink.backend.domain.rental.entity;

import com.campuslink.backend.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity                         // ✅ 이거 꼭 있어야 함!!
@Table(name = "rent_policies")  // ✅ DB 테이블 이름이랑 맞춰줌
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Integer policyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;  // items 테이블의 item_id 외래키

    @Column(name = "min_days")
    private Integer minDays;

    @Column(name = "max_days")
    private Integer maxDays;

    @Column(name = "default_daily_price")
    private Double defaultDailyPrice;

    @Column(name = "default_deposit")
    private Double defaultDeposit;
}
