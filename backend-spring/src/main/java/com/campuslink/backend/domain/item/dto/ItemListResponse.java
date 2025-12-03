package com.campuslink.backend.domain.item.dto;

import com.campuslink.backend.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemListResponse {

    private Integer itemId;
    private String title;
    private String category;
    private Double price;
    private String thumbnailUrl;

    private Integer ownerId;    
    private String ownerName;

    private String status;

    // ⭐ 상세 설명 추가
    private String description;

    public static ItemListResponse from(Item item) {

        // 1) 대표 이미지
        String thumb = null;
        if (item.getImages() != null && !item.getImages().isEmpty()) {
            thumb = item.getImages().get(0).getImageUrl();
        }

        // 2) 소유자 정보
        Integer ownerId = null;
        String ownerName = null;
        if (item.getUser() != null) {
            ownerId = item.getUser().getUserId();
            ownerName = item.getUser().getName();
        }

        // 3) 상태값
        String status = null;
        if (item.getRentAvailable() != null) {
            status = item.getRentAvailable() ? "RENT_AVAILABLE" : "RENT_UNAVAILABLE";
        }

        // 4) DTO 생성
        return ItemListResponse.builder()
                .itemId(item.getItemId())
                .title(item.getTitle())
                .category(item.getCategory())
                .price(item.getPrice())
                .thumbnailUrl(thumb)
                .ownerId(ownerId)
                .ownerName(ownerName)
                .status(status)

                // ⭐ 추가된 부분
                .description(item.getDescription())

                .build();
    }
}
