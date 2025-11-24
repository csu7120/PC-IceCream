package com.campuslink.backend.domain.rental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "qr_logs")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer qrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QrAction action; // PICKUP / RETURN

    @Column(name = "scanned_at")
    private LocalDateTime scannedAt; // null이면 미사용
}
