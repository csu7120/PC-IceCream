package com.campuslink.backend.security;

import com.campuslink.backend.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Integer userId;
    private final String email;
    private final String password;

    public CustomUserDetails(User u) {
        this.userId = u.getUserId();
        this.email = u.getEmail();
        this.password = u.getPassword();
    }

    public Integer getUserId() { return userId; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // 권한 필요시 ROLE_... 추가
    }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
