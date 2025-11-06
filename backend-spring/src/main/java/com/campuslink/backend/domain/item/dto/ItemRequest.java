package com.campuslink.backend.domain.item.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ItemRequest {
    private String name;
    private String description;
    private String category;
    private int price;
    private Integer userId;
    private String ownerEmail;

    // 이미지 파일 받는 필드 (form-data의 key 이름과 동일해야 함)
    private MultipartFile image;
}
