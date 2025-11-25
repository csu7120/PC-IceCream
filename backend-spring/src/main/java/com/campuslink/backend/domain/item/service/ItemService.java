package com.campuslink.backend.domain.item.service;

import com.campuslink.backend.domain.item.dto.ItemListResponse;
import com.campuslink.backend.domain.item.entity.Item;
import com.campuslink.backend.domain.item.entity.ItemImage;
import com.campuslink.backend.domain.item.repository.ItemRepository;
import com.campuslink.backend.domain.rental.entity.RentPolicy;
import com.campuslink.backend.domain.rental.repository.RentPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final RentPolicyRepository rentPolicyRepository;

    private final String uploadPath = System.getProperty("user.dir") + "/uploads/";

    public Item registerItem(Item item, List<MultipartFile> files) throws IOException {

        if (item.getRentAvailable() == null) {
            item.setRentAvailable(true);
        }

        // 1) 이미지 저장 로직
        if (files != null && !files.isEmpty()) {
            List<ItemImage> imageList = new ArrayList<>();

            for (MultipartFile file : files) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                File dest = new File(uploadPath + fileName);

                dest.getParentFile().mkdirs();
                file.transferTo(dest);

                imageList.add(ItemImage.builder()
                        .item(item)
                        .imageUrl("/uploads/" + fileName)
                        .build());
            }
            item.setImages(imageList);
        }

        // 2) 아이템 저장
        Item savedItem = itemRepository.save(item);

        rentPolicyRepository.findByItem_ItemId(savedItem.getItemId())
                .orElseGet(() -> {
                    RentPolicy policy = RentPolicy.builder()
                            .item(savedItem)
                            .minDays(1)
                            .maxDays(14)
                            .defaultDailyPrice(BigDecimal.valueOf(
                                    savedItem.getPrice() != null ? savedItem.getPrice() : 0.0))
                            .defaultDeposit(BigDecimal.ZERO)
                            .build();
                    return rentPolicyRepository.save(policy);
                });

        return savedItem;
    }


    public Page<ItemListResponse> searchItemDtos(
            String keyword,
            String category,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    ) {
        Page<Item> page = itemRepository.search(keyword, category, minPrice, maxPrice, pageable);
        return page.map(ItemListResponse::from);
    }


    public void deleteItem(Integer itemId, Integer userId) {
        // 1) 아이템 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 물품입니다."));

        // 2) 소유자 확인
        if (!item.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인이 등록한 물품만 삭제할 수 있습니다.");
        }

        // 3) RentPolicy 삭제
        rentPolicyRepository.deleteByItem_ItemId(itemId);

        // 4) 이미지 파일 삭제
        if (item.getImages() != null) {
            item.getImages().forEach(img -> {
                try {
                    String imageUrl = img.getImageUrl();
                    String fullPath = System.getProperty("user.dir") + imageUrl;

                    File file = new File(fullPath);
                    if (file.exists()) file.delete();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // 5) DB 삭제(cascade로 item_images 삭제됨)
        itemRepository.delete(item);
    }
}
