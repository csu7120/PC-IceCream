package com.campuslink.backend.domain.user.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.user.dto.UserResponse;
import com.campuslink.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> find(@PathVariable Integer id) {
        UserResponse user = userService.getById(id);
        return ApiResponse.ok(user);
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe(Authentication authentication) {
        String email = authentication.getName(); 

        UserResponse me = userService.getByEmail(email);

        return ApiResponse.ok(me);
    }

    @DeleteMapping("/me")
    public ApiResponse<String> deleteMe(Authentication authentication) {
        String email = authentication.getName();

        userService.deleteByEmail(email);

        return ApiResponse.ok("회원 탈퇴가 완료되었습니다.");
    }
}
