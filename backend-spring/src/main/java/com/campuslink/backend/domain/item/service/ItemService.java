package com.campuslink.backend.domain.item.service;

import com.campuslink.backend.domain.item.entity.Item;
import com.campuslink.backend.domain.item.entity.ItemImage;
import com.campuslink.backend.domain.item.repository.ItemRepository;
import com.campuslink.backend.domain.rental.entity.RentPolicy;
import com.campuslink.backend.domain.rental.repository.RentPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final RentPolicyRepository rentPolicyRepository; // ✅ 추가

    private final String uploadPath = System.getProperty("user.dir") + "/uploads/";

    public Item registerItem(Item item, List<MultipartFile> files) throws IOException {
    	
    	 if (item.getRentAvailable() == null) {
             item.setRentAvailable(true);
         }
    	
        // 1) 이미지 저장 로직(기존 그대로)
        if (files != null && !files.isEmpty()) {
            List<ItemImage> imageList = new ArrayList<>();
            
            

            for (MultipartFile file : files) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                File dest = new File(uploadPath + fileName);

                // 경로가 없으면 생성
                dest.getParentFile().mkdirs();
                file.transferTo(dest);

                imageList.add(ItemImage.builder()
                        .item(item)
                        .imageUrl("/uploads/" + fileName)
                        .build());
            }
            item.setImages(imageList);
        }

        // 2) 아이템 저장(기존 그대로)
        Item savedItem = itemRepository.save(item);

        // 3) ✅ rent_policies 자동 생성
        // 이미 정책이 있으면 만들지 않음(중복 방지)
        rentPolicyRepository.findByItem_ItemId(savedItem.getItemId())
                .orElseGet(() -> {
                    RentPolicy policy = RentPolicy.builder()
                            .item(savedItem)
                            .minDays(1)
                            .maxDays(14)
                            .defaultDailyPrice(
                                    BigDecimal.valueOf(
                                            savedItem.getPrice() != null ? savedItem.getPrice() : 0.0
                                    )
                            )
                            .defaultDeposit(BigDecimal.ZERO)
                            .build();
                    return rentPolicyRepository.save(policy);
                });

        return savedItem;
    }

    public List<Item> searchItems(String keyword) {
        return itemRepository.searchByKeyword(keyword);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
