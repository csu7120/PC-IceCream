package com.campuslink.backend.domain.item.controller;

import com.campuslink.backend.domain.item.service.ItemImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/items/images")
@RequiredArgsConstructor
public class ItemImageController {

    private final ItemImageService itemImageService;

    // ✅ 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String imageUrl = itemImageService.saveImage(file);
        return ResponseEntity.ok(imageUrl);
    }
}
