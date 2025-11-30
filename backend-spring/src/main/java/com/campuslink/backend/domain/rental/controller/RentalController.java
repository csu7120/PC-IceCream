package com.campuslink.backend.domain.rental.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.rental.dto.RentalRequest;
import com.campuslink.backend.domain.rental.dto.RentalResponse;
import com.campuslink.backend.domain.rental.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    // 대여 요청
    @PostMapping
    public ApiResponse<RentalResponse> requestRental(
            @RequestParam String email,
            @RequestBody @Valid RentalRequest req
    ) {
        return ApiResponse.ok(rentalService.requestRental(email, req));
    }

    // 대여 수락
    @PostMapping("/{id}/accept")
    public ApiResponse<RentalResponse> acceptRental(
            @PathVariable Integer id,
            @RequestParam String lenderEmail
    ) {
        return ApiResponse.ok(rentalService.acceptRental(lenderEmail, id));
    }

    // 대여 취소 (요청자든 빌려주는 사람이든 가능하니까 userEmail로 통일)
    @PostMapping("/{id}/cancel")
    public ApiResponse<RentalResponse> cancelRental(
            @PathVariable Integer id,
            @RequestParam String userEmail
    ) {
        return ApiResponse.ok(rentalService.cancelRental(userEmail, id));
    }

    // 픽업(=대여 시작) -> IN_USE로 전환
    @PostMapping("/{id}/pickup")
    public ApiResponse<RentalResponse> pickupRental(
            @PathVariable Integer id,
            @RequestParam String userEmail
    ) {
        return ApiResponse.ok(rentalService.pickupRental(userEmail, id));
    }

    // 반납 -> RETURNED로 전환
    @PostMapping("/{id}/return")
    public ApiResponse<RentalResponse> returnRental(
            @PathVariable Integer id,
            @RequestParam String userEmail
    ) {
        return ApiResponse.ok(rentalService.returnRental(userEmail, id));
    }

    // ✅ 내가 빌린 목록 (프론트/포스트맨이 호출하는 /borrowings/me 로 변경)
    @GetMapping("/borrowings/me")
    public ApiResponse<List<RentalResponse>> myRentals(
            @RequestParam String renterEmail
    ) {
        return ApiResponse.ok(rentalService.myRentals(renterEmail));
    }

    // 내가 빌려준 목록
    @GetMapping("/lendings/me")
    public ApiResponse<List<RentalResponse>> myLendings(
            @RequestParam String lenderEmail
    ) {
        return ApiResponse.ok(rentalService.myLendings(lenderEmail));
    }
}
