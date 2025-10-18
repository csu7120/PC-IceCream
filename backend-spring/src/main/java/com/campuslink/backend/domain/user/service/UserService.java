package com.campuslink.backend.domain.user.service;

import com.campuslink.backend.domain.user.dto.UserResponse;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository users;

    public UserResponse getById(Integer id) {
        User u = users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return new UserResponse(u.getUserId(), u.getEmail(), u.getName(), u.getProfileUrl(), u.getIsVerified());
    }
}
