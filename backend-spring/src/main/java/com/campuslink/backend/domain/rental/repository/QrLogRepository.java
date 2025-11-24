package com.campuslink.backend.domain.rental.repository;

import com.campuslink.backend.domain.rental.entity.QrLog;
import com.campuslink.backend.domain.rental.entity.QrAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrLogRepository extends JpaRepository<QrLog, Integer> {

    // ✅ 기존: 최근 QR 로그 조회(필요하면 유지)
    Optional<QrLog> findTop1ByRental_RentalIdAndActionOrderByScannedAtDesc(Integer rentalId, QrAction action);

    // ✅ 기존: 코드 중복 체크
    boolean existsByCode(String code);

    // ✅ 추가 1) 같은 rental + action에 "미사용 QR"이 이미 있는지 체크
    boolean existsByRental_RentalIdAndActionAndScannedAtIsNull(Integer rentalId, QrAction action);

    // ✅ 추가 2) 스캔할 QR 로그 매칭 조회 (미사용 여부는 서비스에서 검사)
    Optional<QrLog> findByRental_RentalIdAndCodeAndAction(Integer rentalId, String code, QrAction action);
}
