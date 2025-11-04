package com.campuslink.backend.domain.campus.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.campus.dto.CampusResponse;
import com.campuslink.backend.domain.campus.service.CampusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campuses")
@RequiredArgsConstructor
public class CampusController {

    private final CampusService campusService;

    @GetMapping
    public ApiResponse<List<CampusResponse>> getAll() {
        return ApiResponse.ok(campusService.getAll());
    }
}
