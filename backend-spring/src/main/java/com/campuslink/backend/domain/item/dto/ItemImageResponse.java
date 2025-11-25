package com.campuslink.backend.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemImageResponse {
    private Integer imageId;
    private String imageUrl;
}