package com.campuslink.backend.domain.rental.dto;

import com.campuslink.backend.domain.rental.entity.QrAction;
import java.time.LocalDateTime;

public record QrLogResponse(
        Integer qrId,
        Integer rentalId,
        String code,
        QrAction action,
        LocalDateTime scannedAt
) {}
