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
    private Integer ownerId;   // ← 이것 추가해야 함
    private String ownerName;
    private String status;
    
    public static ItemListResponse from(Item item) {
        String thumb = null;

        if (item.getImages() != null && !item.getImages().isEmpty()) {
            thumb = item.getImages().get(0).getImageUrl();
        }

        return ItemListResponse.builder()
                .itemId(item.getItemId())
                .title(item.getTitle())
                .category(item.getCategory())
                .price(item.getPrice())
                .thumbnailUrl(thumb)
                .ownerName(item.getUser() != null ? item.getUser().getName() : null)
                .build();
    }
}
