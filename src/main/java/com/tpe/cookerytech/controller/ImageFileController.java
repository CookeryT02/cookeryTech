package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.domain.ImageFile;
import com.tpe.cookerytech.dto.response.ImageFileResponse;
import com.tpe.cookerytech.dto.response.ImageSavedResponse;
import com.tpe.cookerytech.dto.response.ResponseMessage;
import com.tpe.cookerytech.service.ImageFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageFileController {

    private final ImageFileService imageFileService;


    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }

    @PostMapping("/{modelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(@RequestParam("file")MultipartFile file,
                                                         @PathVariable Long modelId){
        String imageId =  imageFileService.saveImage(file,modelId);

        ImageSavedResponse response =
                new ImageSavedResponse(imageId,ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{modelId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<ImageFileResponse>> getProductImages(@PathVariable Long modelId) {

        List<ImageFileResponse> images = imageFileService.getProductImages(modelId);
        if (images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(images);
    }

}
