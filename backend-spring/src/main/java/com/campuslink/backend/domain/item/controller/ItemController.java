package com.campuslink.backend.domain.item.controller;

import com.campuslink.backend.domain.item.dto.ItemResponse;
import com.campuslink.backend.common.response.ApiResponse;
import com.campuslink.backend.domain.item.dto.ItemListResponse;
import com.campuslink.backend.domain.item.entity.Item;
import com.campuslink.backend.domain.item.service.ItemService;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // ✅ 물품 등록 (이미지 포함)
    @PostMapping(consumes = "multipart/form-data")
    public ApiResponse<ItemResponse> registerItem(   // ← 여기만 수정됨
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("price") Double price,
            @RequestParam("category") String category,
            @RequestParam("userId") Integer userId,
            @RequestPart(value = "images", required = false) List<MultipartFile> files
    ) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

            Item item = Item.builder()
                    .title(title)
                    .description(description)
                    .price(price)
                    .category(category)
                    .user(user)
                    .build();

            Item saved = itemService.registerItem(item, files);

            // 반환 DTO 고정
            return ApiResponse.ok(ItemResponse.from(saved));

        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail("파일 업로드 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail("등록 실패: " + e.getMessage());
        }
    }

    // ✅ 내가 올린 물품 조회
    @GetMapping("/me")
    public ApiResponse<Page<ItemListResponse>> getMyItems(
            @RequestParam("userId") Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ItemListResponse> result = itemService.getMyItems(userId, pageable);
        return ApiResponse.ok(result);
    }

    @DeleteMapping("/{itemId}")
    public ApiResponse<String> deleteItem(
            @PathVariable Integer itemId,
            @RequestParam("userId") Integer userId
    ) {
        try {
            itemService.deleteItem(itemId, userId);
            return ApiResponse.ok("물품이 삭제되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ApiResponse.fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail("삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // ✅ 전체 목록 + 검색 + 페이징
    @GetMapping
    public ApiResponse<Page<ItemListResponse>> getItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort.Direction dir = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        Page<ItemListResponse> result =
                itemService.searchItemDtos(keyword, category, minPrice, maxPrice, pageable);

        return ApiResponse.ok(result);
    }

    // ⭐ 아이템 상세 조회 API
    @GetMapping("/{itemId}")
    public ApiResponse<ItemListResponse> getItemDetail(
            @PathVariable Integer itemId
    ) {
        ItemListResponse result = itemService.getItemDetail(itemId);
        return ApiResponse.ok(result);
    }
}
