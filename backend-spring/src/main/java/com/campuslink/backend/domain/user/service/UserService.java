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

    //(유저 조회 - ID 기준)
    public UserResponse getById(Integer id) {
        User u = users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return new UserResponse(u.getUserId(), u.getEmail(), u.getName(), u.getProfileUrl(), u.getIsVerified());
    }

    //(유저 조회 - 이메일 기준)
    public UserResponse getByEmail(String email) {
        User u = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return new UserResponse(u.getUserId(), u.getEmail(), u.getName(), u.getProfileUrl(), u.getIsVerified());
    }

    //(회원 탈퇴)
    public void deleteByEmail(String email) {
        User user = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));
        users.delete(user);
    }

    //(관리자 삭제)
    public void deleteById(Integer userId) {
        if (!users.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.");
        }
        users.deleteById(userId);
    }

    //(이메일 중복 체크)
    public boolean existsByEmail(String email) {
        return users.existsByEmail(email);  // ✔ 정답
    }
}

