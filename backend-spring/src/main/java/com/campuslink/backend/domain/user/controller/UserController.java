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

    // 🔹 사용자 id로 조회
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> find(@PathVariable Integer id) {
        UserResponse user = userService.getById(id);
        return ApiResponse.ok(user);
    }

    // 🔹 로그인한 사용자의 이메일을 직접 요청 Body 또는 Param으로 받는 방식으로 변경
    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe(@RequestParam String email) {
        UserResponse me = userService.getByEmail(email);
        return ApiResponse.ok(me);
    }

    // 🔹 탈퇴도 email을 파라미터로 받도록 변경
    @DeleteMapping("/me")
    public ApiResponse<String> deleteMe(@RequestParam String email) {
        userService.deleteByEmail(email);
        return ApiResponse.ok("회원 탈퇴가 완료되었습니다.");
    }
    @GetMapping("/check-email")
    public ApiResponse<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ApiResponse.ok(exists);
    }
}
