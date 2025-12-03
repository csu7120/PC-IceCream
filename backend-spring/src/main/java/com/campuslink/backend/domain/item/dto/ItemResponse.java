package com.campuslink.backend.domain.item.dto;

import com.campuslink.backend.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponse {

    private Integer itemId;
    private String title;
    private String description;
    private String category;
    private Double price;
    private Integer ownerId;
    private String ownerName;
    private String thumbnailUrl;  // 첫 번째 이미지
    private String status;

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .itemId(item.getItemId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .price(item.getPrice())
                .ownerId(item.getUser().getUserId())
                .ownerName(item.getUser().getName())
                .thumbnailUrl(
                        item.getImages() != null && !item.getImages().isEmpty()
                                ? item.getImages().get(0).getImageUrl()
                                : null
                )
                // status 제거
                .build();
    }

}
