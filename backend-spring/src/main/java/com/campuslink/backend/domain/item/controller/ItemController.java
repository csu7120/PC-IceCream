package com.campuslink.backend.domain.item.controller;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

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

    @DeleteMapping("/{itemId}")
    public ApiResponse<String> deleteItem(
            @PathVariable Integer itemId,
            @RequestParam("userId") Integer userId
    ) {
        try {
            itemService.deleteItem(itemId, userId);
            return ApiResponse.ok("물품이 삭제되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 존재하지 않는 물품 / 권한 없는 경우
            return ApiResponse.fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail("삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // ✅ 목록 + 검색 + 페이징 (DTO로 리턴)
    @GetMapping
    public ApiResponse<Page<ItemListResponse>> getItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,   // createdAt, price, title 등
            @RequestParam(defaultValue = "desc") String direction       // asc or desc
    ) {
        // 정렬 방향
        Sort.Direction dir = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // 정렬 기준 필드
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        Page<ItemListResponse> result =
                itemService.searchItemDtos(keyword, category, minPrice, maxPrice, pageable);

        return ApiResponse.ok(result);
    }
}
