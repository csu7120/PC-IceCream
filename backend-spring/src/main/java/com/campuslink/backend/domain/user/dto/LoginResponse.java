package com.campuslink.backend.domain.user.dto;

public record LoginResponse(
        String accessToken,
        Long   expiresInSeconds,
        UserResponse user
) {}
