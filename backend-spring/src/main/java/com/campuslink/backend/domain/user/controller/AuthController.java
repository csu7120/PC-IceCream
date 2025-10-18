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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
    // ✅ 회원가입
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signup(@RequestBody @Valid SignupRequest req) {
        LoginResponse res = authService.signup(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
