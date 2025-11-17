package com.campuslink.backend.domain.item.controller;

import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.item.entity.Item;
import com.campuslink.backend.domain.item.service.ItemService;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserRepository userRepository;

    @PostMapping(consumes = "multipart/form-data")
    public ApiResponse<Item> registerItem(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("price") Double price,
            @RequestParam("category") String category,
            @RequestParam("userId") Integer userId,
            @RequestPart(value = "images", required = false) List<MultipartFile> files
    ) {
        try {
            // 사용자 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

            // Item 엔티티 생성
            Item item = Item.builder()
                    .title(title)
                    .description(description)
                    .price(price)
                    .category(category)
                    .user(user)
                    .build();

            // 이미지 포함 저장
            Item saved = itemService.registerItem(item, files);

            return ApiResponse.ok(saved);

        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail("파일 업로드 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail("등록 실패: " + e.getMessage());
        }
    }
}
