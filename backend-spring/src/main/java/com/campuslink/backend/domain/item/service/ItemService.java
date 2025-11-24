package com.campuslink.backend.domain.item.service;

import com.campuslink.backend.domain.item.dto.ItemListResponse;
import com.campuslink.backend.domain.item.entity.Item;
import com.campuslink.backend.domain.item.entity.ItemImage;
import com.campuslink.backend.domain.item.repository.ItemRepository;
import com.campuslink.backend.domain.rental.repository.RentPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
        return itemRepository.save(item);
    }
    

    // 🔥 목록 + 검색 + 페이징을 한 번에 담당
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

        // 2) 소유자 확인 (내 물품인지 체크)
        if (!item.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인이 등록한 물품만 삭제할 수 있습니다.");
        }
        
        rentPolicyRepository.deleteByItem_ItemId(itemId);
        // 3) 이미지 파일 삭제 (파일 시스템)
        if (item.getImages() != null) {
            item.getImages().forEach(img -> {
                try {
                    String imageUrl = img.getImageUrl();  
                    String fullPath = System.getProperty("user.dir") + imageUrl;

                    File file = new File(fullPath);
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {
                   e.printStackTrace();
                }
            });
        }

        // 4) DB에서 아이템 삭제 (cascade 로 item_images도 같이 삭제)
        itemRepository.delete(item);
    }
}
