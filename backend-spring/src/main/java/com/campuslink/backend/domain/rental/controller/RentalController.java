package com.campuslink.backend.domain.rental.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.rental.dto.RentalRequest;
import com.campuslink.backend.domain.rental.dto.RentalResponse;
import com.campuslink.backend.domain.rental.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    // ------------------------------------------------------
    // 1. 대여 요청
    // ------------------------------------------------------
    @PostMapping
    public ApiResponse<RentalResponse> requestRental(
            @RequestParam String email,
            @RequestBody @Valid RentalRequest req
    ) {
        return ApiResponse.ok(rentalService.requestRental(email, req));
    }

    // ------------------------------------------------------
    // 2. 대여 수락 (JWT 인증 기반)
    // ------------------------------------------------------
    @PostMapping("/{id}/accept")
    public ApiResponse<RentalResponse> acceptRental(
            @PathVariable Integer id,
            Authentication auth
    ) {

        System.out.println("🔥 [ACCEPT] API called → rentalId = " + id);

        if (auth == null) {
            System.out.println("❌ [ACCEPT] Authentication is NULL — JWT가 전달되지 않음!");
            throw new RuntimeException("JWT Authentication is missing.");
        }

        String lenderEmail = auth.getName(); // JWT subject = email
        System.out.println("🔥 [ACCEPT] Authenticated user email = " + lenderEmail);

        return ApiResponse.ok(rentalService.acceptRental(lenderEmail, id));
    }

    // ------------------------------------------------------
    // 3. 대여 취소
    // ------------------------------------------------------
    @PostMapping("/{id}/cancel")
    public ApiResponse<RentalResponse> cancelRental(
            @PathVariable Integer id,
            @RequestParam String userEmail
    ) {
        return ApiResponse.ok(rentalService.cancelRental(userEmail, id));
    }

    // ------------------------------------------------------
    // 4. 픽업(대여 시작)
    // ------------------------------------------------------
    @PostMapping("/{id}/pickup")
    public ApiResponse<RentalResponse> pickupRental(
            @PathVariable Integer id,
            @RequestParam String userEmail
    ) {
        return ApiResponse.ok(rentalService.pickupRental(userEmail, id));
    }

    // ------------------------------------------------------
    // 5. 반납
    // ------------------------------------------------------
    @PostMapping("/{id}/return")
    public ApiResponse<RentalResponse> returnRental(
            @PathVariable Integer id,
            @RequestParam String userEmail
    ) {
        return ApiResponse.ok(rentalService.returnRental(userEmail, id));
    }

    // ------------------------------------------------------
    // 6. 내가 빌린 목록
    // ------------------------------------------------------
    @GetMapping("/borrowings/me")
    public ApiResponse<List<RentalResponse>> myRentals(
            @RequestParam String renterEmail
    ) {
        return ApiResponse.ok(rentalService.myRentals(renterEmail));
    }

    // ------------------------------------------------------
    // 7. 내가 빌려준 목록
    // ------------------------------------------------------
    @GetMapping("/lendings/me")
    public ApiResponse<List<RentalResponse>> myLendings(
            @RequestParam String lenderEmail
    ) {
        return ApiResponse.ok(rentalService.myLendings(lenderEmail));
    }
}
