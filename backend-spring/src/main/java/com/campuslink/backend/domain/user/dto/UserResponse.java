package com.campuslink.backend.domain.user.dto;

import com.campuslink.backend.domain.user.entity.User;

public record UserResponse(
        Integer userId,
        String email,
        String name,
        String profileUrl,
        Boolean isVerified
) {
    public static UserResponse from(User u) {
        return new UserResponse(
                u.getUserId(),
                u.getEmail(),
                u.getName(),
                u.getProfileUrl(),
                u.getIsVerified()
        );
    }
}
