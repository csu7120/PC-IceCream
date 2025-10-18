package com.campuslink.backend.domain.user.service;

import com.campuslink.backend.domain.user.dto.LoginRequest;
import com.campuslink.backend.domain.user.dto.LoginResponse;
import com.campuslink.backend.domain.user.dto.SignupRequest;
import com.campuslink.backend.domain.user.dto.UserResponse; 
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import com.campuslink.backend.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${jwt.access-exp-seconds}")
    private long expires;

    
    public LoginResponse login(LoginRequest req) {
    	System.out.println("RAW=" + req.password());
        User u = users.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        System.out.println("HASH_DB=" + u.getPassword());
        System.out.println("ENCODER=" + passwordEncoder.getClass().getName());
        System.out.println("MATCH=" + passwordEncoder.matches(req.password(), u.getPassword()));
        if (!passwordEncoder.matches(req.password(), u.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }
        String token = jwtProvider.generateToken(u.getEmail());
        return new LoginResponse(token, expires, UserResponse.from(u));
    }
    
    
    public LoginResponse signup(SignupRequest req) {
        if (users.existsByEmail(req.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        
        User u = new User();
        u.setCampusId(req.campusId());
        u.setEmail(req.email());
        u.setPassword(passwordEncoder.encode(req.password())); 
        u.setName(req.name());
        u.setPhone(req.phone());           
        
        users.save(u);
        
        String token = jwtProvider.generateToken(u.getEmail());
        return new LoginResponse(token, expires, UserResponse.from(u));
    }
}

