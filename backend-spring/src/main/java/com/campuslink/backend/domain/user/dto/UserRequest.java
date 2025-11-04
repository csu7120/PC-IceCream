package com.campuslink.backend.domain.user.dto;

public record UserRequest(
        Integer campusId,
        String email,
        String password,
        String name,
        String phone
) {}
