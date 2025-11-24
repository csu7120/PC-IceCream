package com.campuslink.backend.domain.rental.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.rental.dto.QrLogResponse;
import com.campuslink.backend.domain.rental.dto.QrScanRequest;
import com.campuslink.backend.domain.rental.entity.QrAction;
import com.campuslink.backend.domain.rental.service.QrService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentals/{id}/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;

    // ✅ QR 생성 (lender가 생성)
    // POST /api/rentals/{id}/qr/generate?action=PICKUP&userEmail=xxx
    @PostMapping("/generate")
    public ApiResponse<String> generate(@PathVariable Integer id,
                                        @RequestParam QrAction action,
                                        @RequestParam String userEmail) {
        return ApiResponse.ok(qrService.generateQr(id, action, userEmail));
    }

    // ✅ QR 스캔 (픽업/반납 확정)
    // POST /api/rentals/{id}/qr/scan?userEmail=xxx
    @PostMapping("/scan")
    public ApiResponse<QrLogResponse> scan(@PathVariable Integer id,
                                           @RequestParam String userEmail,
                                           @Valid @RequestBody QrScanRequest req) {
        return ApiResponse.ok(qrService.scan(id, userEmail, req));
    }
}
