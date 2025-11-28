package com.campuslink.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    // yml의 access-exp-seconds랑 이름 맞춤 (단위: 초)
    @Value("${jwt.access-exp-seconds}")
    private long accessExpSeconds;

    private Key key;

    @PostConstruct
    public void initKey() {
        // secret 길이는 최소 32자 이상 권장
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성 (subject = 이메일)
    public String generateToken(String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpSeconds * 1000L); // 초 → ms

        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 subject(email) 꺼내기
    public String getSubject(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    // 필요하면 만료 시간(초) 외부에서 참고
    public long getAccessExpSeconds() {
        return accessExpSeconds;
    }
}
