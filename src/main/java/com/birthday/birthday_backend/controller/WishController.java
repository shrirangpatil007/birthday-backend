package com.birthday.birthday_backend.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.birthday.birthday_backend.model.Wish;
import com.birthday.birthday_backend.repository.WishRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class WishController {

    private final WishRepository wishRepository;
    private final Cloudinary cloudinary;

    
    @PostMapping
    public ResponseEntity<Wish> addWish(
            @RequestParam("name") String name,
            @RequestParam("message") String message,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {

        Wish wish = new Wish();
        wish.setName(name);
        wish.setMessage(message);
        wish.setDate(LocalDateTime.now().toLocalDate());

        if(image != null && !image.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            System.out.println("Cloudinary Upload Response: " + uploadResult); // ← Add this
            Object secureUrlObj = uploadResult.get("secure_url");

            if (secureUrlObj != null) {
                wish.setImageUrl(secureUrlObj.toString());
            } else {
                System.out.println("❗ secure_url not found in Cloudinary response");
            }

        }

        Wish savedWish = wishRepository.save(wish);
        return ResponseEntity.ok(savedWish);

    }

    @GetMapping
    public List<Wish> getAllWishes() {
        return wishRepository.findAll();
    }


}
