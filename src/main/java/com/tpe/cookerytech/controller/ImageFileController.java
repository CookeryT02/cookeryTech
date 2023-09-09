package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.domain.ImageFile;
import com.tpe.cookerytech.service.ImageFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageFileController {

    private final ImageFileService imageFileService;

    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }



    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<ImageFile>> getProductImages(@PathVariable Long productId) {

        List<ImageFile> images = imageFileService.getProductImages(productId);
        if (images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(images);
    }



}
