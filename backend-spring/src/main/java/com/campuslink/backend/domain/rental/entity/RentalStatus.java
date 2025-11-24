package com.campuslink.backend.domain.rental.entity;

public enum RentalStatus {
	REQUESTED,   // 대여 요청
    ACCEPTED,    // 대여자가 요청 승인
    PICKED_UP,   // 수령 완료(QR)
    IN_USE,      // 사용중 (선택)
    RETURNED,    // 반납 완료
    LATE,        // 연체(선택)
    CANCELLED    // 취소
}
