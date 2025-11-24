package com.campuslink.backend.domain.rental.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RentalRequest(
        @NotNull Integer itemId,
        @NotNull LocalDateTime startAt,
        @NotNull LocalDateTime endAt
) {}
