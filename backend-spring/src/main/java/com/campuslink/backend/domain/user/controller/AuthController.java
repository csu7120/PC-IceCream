package com.campuslink.backend.domain.user.controller;

import com.campuslink.backend.domain.user.dto.SignupRequest;
import com.campuslink.backend.domain.user.dto.LoginRequest;
import com.campuslink.backend.domain.user.dto.LoginResponse;
import com.campuslink.backend.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signup(@RequestBody @Valid SignupRequest req) {
        LoginResponse res = authService.signup(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 로그인 + 토큰 발급
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest req) {
        LoginResponse res = authService.login(req);
        return ResponseEntity.ok(res);
    }
}
