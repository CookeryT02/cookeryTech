package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.ImageData;
import com.tpe.cookerytech.domain.ImageFile;
import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.dto.response.ImageSavedResponse;
import com.tpe.cookerytech.dto.response.ResponseMessage;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.repository.ImageFileRepository;
import com.tpe.cookerytech.repository.ModelRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class ImageFileService {


    private final ImageFileRepository imageFileRepository;

    private final ModelRepository modelRepository;


    public ImageFileService(ImageFileRepository imageFileRepository, ModelRepository modelRepository) {
        this.imageFileRepository = imageFileRepository;
        this.modelRepository = modelRepository;
    }

    public String saveImage(MultipartFile file,Long id) {
        ImageFile imageFile = null;

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            ImageData imageData = new ImageData(file.getBytes());
            imageFile = new ImageFile(fileName,file.getContentType(),imageData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        Model model = modelRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION));
        imageFile.setModelId(model);
        imageFileRepository.save(imageFile);

        return imageFile.getId();
    }

//    public List<ImageSavedResponse> saveImages(MultipartFile[] files, Long modelId) {
//        List<ImageSavedResponse> responses = new ArrayList<>();
//        for (MultipartFile file:files){
//            String imageId = saveImage(file,modelId);
//            ImageSavedResponse response = new ImageSavedResponse(imageId, ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE,true);
//            responses.add(response);
//        }
//        return responses;
//    }

    public List<ImageFile> getProductImages(Long modelId) {

        Model model = modelRepository.findById(modelId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,modelId)));

        Set<ImageFile> images = model.getImage();

        // Resim setini bir liste olarak dönüştürün
        List<ImageFile> imageList = new ArrayList<>(images);

        return imageList;

    }
}
