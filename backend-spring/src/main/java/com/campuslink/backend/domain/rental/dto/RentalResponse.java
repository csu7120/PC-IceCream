package com.campuslink.backend.domain.rental.dto;

import com.campuslink.backend.domain.item.entity.Item;
import com.campuslink.backend.domain.rental.entity.Rental;
import com.campuslink.backend.domain.rental.entity.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RentalResponse(
        Integer rentalId,
        Integer itemId,
        Integer lenderId,
        Integer renterId,
        LocalDateTime startAt,
        LocalDateTime endAt,
        BigDecimal dailyPrice,
        BigDecimal deposit,
        RentalStatus status,
        LocalDateTime pickedUpAt,
        LocalDateTime returnedAt,
        BigDecimal lateFee,
        LocalDateTime createdAt,

        // ⭐ 기존 추가 필드들
        String itemTitle,
        String itemImageUrl,
        BigDecimal itemOriginalPrice,

        // ⭐ 여기에 ownerName 추가 (핵심)
        String ownerName
) {

    public static RentalResponse from(Rental rental) {

        Item item = rental.getItem();

        // 대표 이미지 추출
        String imageUrl = null;
        if (item.getImages() != null && !item.getImages().isEmpty()) {
            imageUrl = item.getImages().get(0).getImageUrl();
        }

        // ⭐ ownerName: 아이템 등록한 사람의 닉네임 or 이름
        String ownerName = item.getUser().getName() != null
                ? item.getUser().getName()
                : item.getUser().getName();

        return new RentalResponse(
                rental.getRentalId(),
                item.getItemId(),
                rental.getLender().getUserId(),
                rental.getRenter().getUserId(),
                rental.getStartAt(),
                rental.getEndAt(),
                rental.getDailyPrice(),
                rental.getDeposit(),
                rental.getStatus(),
                rental.getPickedUpAt(),
                rental.getReturnedAt(),
                rental.getLateFee(),
                rental.getCreatedAt(),

                item.getTitle(),
                imageUrl,
                BigDecimal.valueOf(item.getPrice()),

                // ⭐ 추가된 ownerName
                ownerName
        );
    }
}
