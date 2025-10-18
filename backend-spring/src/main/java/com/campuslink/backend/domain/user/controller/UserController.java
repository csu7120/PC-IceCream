package com.campuslink.backend.domain.user.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.user.dto.UserResponse;
import com.campuslink.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> find(@PathVariable Integer id) { 
        return ApiResponse.ok(userService.getById(id));
    }
}
