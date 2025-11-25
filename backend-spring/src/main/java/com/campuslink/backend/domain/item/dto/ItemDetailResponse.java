package com.campuslink.backend.domain.item.dto;

import com.campuslink.backend.domain.user.dto.UserSimpleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ItemDetailResponse {
    private Integer itemId;
    private String title;
    private String description;
    private Double price;
    private String category;
    private LocalDateTime createdAt;

    private UserSimpleResponse owner;
    private List<ItemImageResponse> images;
}