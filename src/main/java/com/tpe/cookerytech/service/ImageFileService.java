package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.ImageData;
import com.tpe.cookerytech.domain.ImageFile;
import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.dto.response.ImageFileResponse;
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
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        imageFile.setModel(model);
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

    public List<ImageFileResponse> getProductImages(Long modelId) {

        Model model = modelRepository.findById(modelId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION, modelId)));

        List<ImageFileResponse> imageResponses = new ArrayList<>();
        for (ImageFile imageFile : model.getImage()) {

            ImageFileResponse imageFileResponse = convertToResponse(imageFile);
            imageResponses.add(imageFileResponse);
        }

        return imageResponses;


    }

    public static ImageFileResponse convertToResponse(ImageFile imageFile) {

        ImageFileResponse response = new ImageFileResponse();

        String imageUri = ServletUriComponentsBuilder.
                fromCurrentContextPath(). // localhost:8080
                        path("/images/download/"). // localhost:8080/images/download
                        path(imageFile.getId()).toUriString();

        response.setName(imageFile.getName());
        response.setUrl(imageUri);
        response.setSize(imageFile.getLength());
        response.setType(imageFile.getType());

        return response;
    }

    public Boolean removeById(String id) {
        ImageFile imageFile =  getImageById(id);

        if(imageFileRepository.existsById(id)){
            imageFileRepository.delete(imageFile);
            return true;
        }else {
            return false;
        }
    }

    public ImageFile getImageById(String id) {
        ImageFile imageFile = imageFileRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(
                        String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE,id)));
        return imageFile ;
    }



}
