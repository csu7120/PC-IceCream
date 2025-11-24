package com.campuslink.backend.common.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    ITEM_NOT_FOUND("아이템을 찾을 수 없습니다."),
    ITEM_NOT_RENTABLE("해당 아이템은 대여할 수 없습니다."),
    RENT_SELF_NOT_ALLOWED("자기 자신의 아이템은 대여할 수 없습니다."),
    RENT_DATE_INVALID("대여 날짜가 올바르지 않습니다."),
    RENT_POLICY_NOT_FOUND("대여 정책을 찾을 수 없습니다."),
    RENT_DAYS_OUT_OF_RANGE("대여 가능 기간을 초과했습니다."),
    RENT_ALREADY_IN_PROGRESS("이미 대여중인 아이템입니다."),
    RENT_STATUS_INVALID("현재 상태에서는 처리가 불가능합니다."),
    RENT_CANNOT_CANCEL_NOW("현재 상태에서는 취소할 수 없습니다."),
    RENTAL_NOT_FOUND("대여 정보를 찾을 수 없습니다."),
    FORBIDDEN("권한이 없습니다."),

    // ✅ QR 전용 에러
    QR_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 미사용 QR이 존재합니다."),
    QR_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 사용된 QR입니다."),
    QR_NOT_FOUND(HttpStatus.NOT_FOUND, "QR 정보를 찾을 수 없습니다.");

    private final String message;
    private final HttpStatus status;

    // ⬅ 기존 에러용 (HttpStatus 없을 때)
    ErrorCode(String message) {
        this.status = HttpStatus.BAD_REQUEST;  // 기본 HttpStatus
        this.message = message;
    }

    // ⬅ QR 에러용 (HttpStatus + message)
    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
