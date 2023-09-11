package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.ImageData;
import com.tpe.cookerytech.domain.ImageFile;
import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.dto.response.ImageFileDTO;
import com.tpe.cookerytech.dto.response.ImageSavedResponse;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.ImageMapper;
import com.tpe.cookerytech.repository.ImageFileRepository;
import com.tpe.cookerytech.repository.ModelRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class ImageFileService {


    private final ImageFileRepository imageFileRepository;

    private final ModelRepository modelRepository;


    private final ImageMapper imageMapper;
    public ImageFileService(ImageFileRepository imageFileRepository, ModelRepository modelRepository, ImageMapper imageMapper) {
        this.imageFileRepository = imageFileRepository;
        this.modelRepository = modelRepository;
        this.imageMapper = imageMapper;
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

    public List<ImageFileDTO> displayImage(Long modelId) {
        List<ImageFile> imageFiles=imageFileRepository.findAllByModelId(modelId);
        Model model = modelRepository.findById(modelId).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION));
        List<ImageFileDTO> imageFileDTOS =imageFiles.stream().map(imFile->{
            // URI olusturulmasini sagliyacagiz
            String imageUri = ServletUriComponentsBuilder.
                    fromCurrentContextPath(). // localhost:8080
                            path("/files/download/"). // localhost:8080/files/download
                            path(imFile.getId()).toUriString();// localhost:8080/files/download/id
            return new ImageFileDTO(imFile.getName(),
                    imageUri,
                    imFile.getType(),
                    imFile.getLength());

        }).collect(Collectors.toList());
        return imageFileDTOS;

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


//    public List<ImageSavedResponse> saveImages(MultipartFile[] files, Long modelId) {
//        List<ImageSavedResponse> responses = new ArrayList<>();
//        for (MultipartFile file:files){
//            String imageId = saveImage(file,modelId);
//            ImageSavedResponse response = new ImageSavedResponse(imageId, ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE,true);
//            responses.add(response);
//        }
//        return responses;
//    }
}
