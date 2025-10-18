package com.campuslink.backend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotNull Integer campusId,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 4, max = 50) String password,
        @NotBlank String name,
        String phone
) {}
