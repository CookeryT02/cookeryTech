package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.response.CkResponse;
import com.tpe.cookerytech.dto.response.ImageFileResponse;
import com.tpe.cookerytech.dto.response.ImageSavedResponse;
import com.tpe.cookerytech.dto.response.ResponseMessage;
import com.tpe.cookerytech.service.ImageFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageFileController {

    private final ImageFileService imageFileService;


    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }



    //H01 -> It will get an image of a product    Page:85
    @GetMapping("/{modelId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<ImageFileResponse>> getProductImages(@PathVariable Long modelId) {

        List<ImageFileResponse> images = imageFileService.getProductImages(modelId);
        if (images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(images);
    }





    //H02 -> It will upload image(s) of a product
    @PostMapping("/{modelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(@RequestParam("file")MultipartFile file,
                                                         @PathVariable Long modelId){
        String imageId =  imageFileService.saveImage(file,modelId);

        ImageSavedResponse response =
                new ImageSavedResponse(imageId,ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(response);
    }




    //H03 -> It will delete an image
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CkResponse> deleteImageFile(@PathVariable String id) {
        CkResponse response;
        Boolean isRemoveImage=imageFileService.removeById(id);
        if(isRemoveImage) {
            response = new CkResponse(
                    ResponseMessage.IMAGE_DELETED_RESPONSE_MESSAGE, true);
        }else{
            response = new CkResponse(
                    ResponseMessage.IMAGE_NOT_FOUND_MESSAGE, false);
        }
        return ResponseEntity.ok(response);
    }

}
