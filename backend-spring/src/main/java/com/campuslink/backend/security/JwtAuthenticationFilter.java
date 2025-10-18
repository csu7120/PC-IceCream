package com.campuslink.backend.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String token = resolveToken(req);
        if (StringUtils.hasText(token)) {
            try {
                String email = jwtProvider.getSubject(token);
                var userDetails = userDetailsService.loadUserByUsername(email);
                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                // 토큰 문제 시 인증 미설정(401은 SecurityConfig에서 보호 자원 접근 시 응답)
            }
        }
        chain.doFilter(req, res);
    }

    private String resolveToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (!StringUtils.hasText(header)) return null;
        if (header.startsWith("Bearer ")) return header.substring(7);
        return null;
    }
}
