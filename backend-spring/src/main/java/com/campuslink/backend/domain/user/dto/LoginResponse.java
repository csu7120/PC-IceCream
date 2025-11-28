package com.campuslink.backend.domain.user.dto;

import com.campuslink.backend.domain.user.entity.User;

public record LoginResponse(
        String token,
        long expiresInMinutes,
        Integer userId,
        String email,
        String name
) {
    public static LoginResponse of(String token, long expiresInMinutes, User user) {
        return new LoginResponse(
                token,
                expiresInMinutes,
                user.getUserId(),
                user.getEmail(),
                user.getName()
        );
    }
}
