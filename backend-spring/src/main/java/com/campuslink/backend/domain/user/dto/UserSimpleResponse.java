package com.campuslink.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSimpleResponse {
    private Integer userId;
    private String name;
    private String email;
}
