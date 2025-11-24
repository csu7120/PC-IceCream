package com.campuslink.backend.domain.rental.dto;

import com.campuslink.backend.domain.rental.entity.QrAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QrScanRequest(
        @NotNull QrAction action,
        @NotBlank String code
) {}
