// controller/UserController.java
package com.campuslink.backend.domain.user.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository){ this.userRepository = userRepository; }

    @GetMapping("/{id}")
    public ApiResponse<User> find(@PathVariable Long id){
        return ApiResponse.ok(userRepository.findById(id).orElseThrow());
    }
}
