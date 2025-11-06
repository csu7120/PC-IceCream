package com.campuslink.backend.domain.item.service;

import com.campuslink.backend.domain.item.entity.Item;
import com.campuslink.backend.domain.item.entity.ItemImage;
import com.campuslink.backend.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final String uploadPath = System.getProperty("user.dir") + "/uploads/";

    public Item registerItem(Item item, List<MultipartFile> files) throws IOException {
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
        return itemRepository.save(item);
    }

    public List<Item> searchItems(String keyword) {
        return itemRepository.searchByKeyword(keyword);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
