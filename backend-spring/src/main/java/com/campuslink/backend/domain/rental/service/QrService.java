package com.campuslink.backend.domain.rental.service;

import com.campuslink.backend.common.exception.BusinessException;
import com.campuslink.backend.common.exception.ErrorCode;
import com.campuslink.backend.domain.rental.dto.QrLogResponse;
import com.campuslink.backend.domain.rental.dto.QrScanRequest;
import com.campuslink.backend.domain.rental.entity.*;
import com.campuslink.backend.domain.rental.repository.QrLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrService {

    private final RentalService rentalService;
    private final QrLogRepository qrLogRepository;

    /**
     * ✅ QR 생성
     * - lender만 생성 가능
     * - 생성 시 qr_logs에 저장 (scannedAt=null)
     * - 같은 action에 대해 미사용 QR이 이미 있으면 재발급 막음(1회성)
     */
    @Transactional
    public String generateQr(Integer rentalId, QrAction action, String requesterEmail) {
        Rental rental = rentalService.getRental(rentalId);

        // lender만 QR 생성 가능
        if (!rental.getLender().getEmail().equals(requesterEmail)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 상태별 허용 action 체크
        if (action == QrAction.PICKUP) {
            if (rental.getStatus() != RentalStatus.ACCEPTED) {
                throw new BusinessException(ErrorCode.RENT_STATUS_INVALID);
            }
        } else { // RETURN
            if (rental.getStatus() != RentalStatus.IN_USE &&
                rental.getStatus() != RentalStatus.LATE) {
                throw new BusinessException(ErrorCode.RENT_STATUS_INVALID);
            }
        }

        // 같은 rental+action 미사용 QR이 이미 있으면 재발급 금지
        if (qrLogRepository.existsByRental_RentalIdAndActionAndScannedAtIsNull(rentalId, action)) {
            throw new BusinessException(ErrorCode.QR_ALREADY_EXISTS); 
            // 없으면 RENT_STATUS_INVALID 같은 걸로 대체해도 됨
        }

        // 유니크 코드 생성
        String code;
        do {
            code = UUID.randomUUID().toString();
        } while (qrLogRepository.existsByCode(code));

        // ✅ QR 발급 로그 저장 (아직 스캔 전)
        QrLog log = QrLog.builder()
                .rental(rental)
                .code(code)
                .action(action)
                .scannedAt(null)     // 미사용 상태
                .build();

        qrLogRepository.save(log);
        return code;
    }

    /**
     * ✅ QR 스캔
     * - code/action/rentalId 매칭되는 “미사용 QR”만 허용
     * - PICKUP이면 renter가 스캔 → RentalService.pickupRental() 호출
     * - RETURN이면 lender가 스캔 → RentalService.returnRental() 호출
     */
    @Transactional
    public QrLogResponse scan(Integer rentalId, String scannerEmail, QrScanRequest req) {
        Rental rental = rentalService.getRental(rentalId);

        // QR 로그 검증 (code + action + rentalId)
        QrLog log = qrLogRepository
                .findByRental_RentalIdAndCodeAndAction(rentalId, req.code(), req.action())
                .orElseThrow(() -> new BusinessException(ErrorCode.QR_NOT_FOUND));

        // 이미 스캔된 QR이면 재사용 불가
        if (log.getScannedAt() != null) {
            throw new BusinessException(ErrorCode.QR_ALREADY_USED);
        }

        // 액션별 권한 + 상태전이 실행(기존 RentalService 재사용)
        if (req.action() == QrAction.PICKUP) {

            if (!rental.getRenter().getEmail().equals(scannerEmail)) {
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }

            // ✅ 실제 픽업 처리(아이템 상태 false 포함)
            rentalService.pickupRental(scannerEmail, rentalId);

        } else { // RETURN

            if (!rental.getLender().getEmail().equals(scannerEmail)) {
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }

            // ✅ 실제 반납 처리(lateFee 계산 + item true 복귀 포함)
            rentalService.returnRental(scannerEmail, rentalId);
        }

        // QR 사용 처리
        log.setScannedAt(LocalDateTime.now());
        qrLogRepository.save(log);

        return new QrLogResponse(
                log.getQrId(),
                rentalId,
                log.getCode(),
                log.getAction(),
                log.getScannedAt()
        );
    }
}
