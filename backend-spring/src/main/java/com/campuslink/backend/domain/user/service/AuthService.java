package com.campuslink.backend.domain.user.service;

import com.campuslink.backend.domain.user.dto.*;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import com.campuslink.backend.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private final long expiresInMinutes = 60L; // JwtProvider 설정과 맞추기

    // 회원가입
    public LoginResponse signup(SignupRequest req) {

        if (userRepository.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .name(req.name())
                .campusId(req.campusId())
                .build();

        User saved = userRepository.save(user);

        String token = jwtProvider.generateToken(saved.getEmail());

        return LoginResponse.of(token, expiresInMinutes, saved);
    }

    // 로그인
    public LoginResponse login(LoginRequest req) {

        // Spring Security 인증 시도 (비밀번호 검증)
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                req.email(), req.password()
        );

        Authentication auth = authenticationManager.authenticate(authToken);

        // 여기까지 통과하면 로그인 성공
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));

        String token = jwtProvider.generateToken(user.getEmail());

        return LoginResponse.of(token, expiresInMinutes, user);
    }
}
