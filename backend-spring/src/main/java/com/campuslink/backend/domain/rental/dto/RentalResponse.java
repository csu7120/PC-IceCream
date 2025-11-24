package com.campuslink.backend.domain.rental.dto;

import com.campuslink.backend.domain.rental.entity.RentalStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RentalResponse(
        Integer rentalId,
        Integer itemId,
        Integer lenderId,
        Integer renterId,
        LocalDateTime startAt,
        LocalDateTime endAt,
        BigDecimal dailyPrice,
        BigDecimal deposit,
        RentalStatus status,
        LocalDateTime pickedUpAt,
        LocalDateTime returnedAt,
        BigDecimal lateFee,
        LocalDateTime createdAt
) {}
