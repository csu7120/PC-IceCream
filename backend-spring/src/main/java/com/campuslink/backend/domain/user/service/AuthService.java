package com.campuslink.backend.domain.user.service;

import com.campuslink.backend.domain.campus.service.CampusService;
import com.campuslink.backend.domain.user.dto.LoginRequest;
import com.campuslink.backend.domain.user.dto.LoginResponse;
import com.campuslink.backend.domain.user.dto.SignupRequest;
import com.campuslink.backend.domain.user.dto.UserResponse;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository users;
    private final CampusService campusService;

    @Value("${jwt.access-exp-seconds:3600}")
    private long expires; // 있어도 되고 없어도 됨

    // ✅ JWT, PasswordEncoder 완전 제거

    public LoginResponse login(LoginRequest req) {
        User u = users.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 🔸 단순 문자열 비교
        if (!req.password().equals(u.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        // 🔸 임시 토큰 (지금은 JWT 안 씀)
        String fakeToken = "dev-mode-no-token";

        return new LoginResponse(fakeToken, expires, UserResponse.from(u));
    }

    public LoginResponse signup(SignupRequest req) {
        if (users.existsByEmail(req.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        campusService.validateEmailDomain(req.campusId(), req.email());

        User u = new User();
        u.setCampusId(req.campusId());
        u.setEmail(req.email());
        u.setPassword(req.password()); // 🔸 평문 저장
        u.setName(req.name());
        u.setPhone(req.phone());
        u.setIsVerified(true);

        users.save(u);

        // 🔸 임시 토큰 반환
        String fakeToken = "dev-mode-no-token";

        return new LoginResponse(fakeToken, expires, UserResponse.from(u));
    }
}
