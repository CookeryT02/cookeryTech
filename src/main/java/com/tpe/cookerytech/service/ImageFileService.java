package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.ImageFile;
import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.repository.ImageFileRepository;
import com.tpe.cookerytech.repository.ModelRepository;
import com.tpe.cookerytech.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    private final ModelRepository modelRepository;

    public ImageFileService(ImageFileRepository imageFileRepository, ModelRepository modelRepository) {
        this.imageFileRepository = imageFileRepository;
        this.modelRepository = modelRepository;
    }


    public List<ImageFile> getProductImages(Long modelId) {

        Model model = modelRepository.findById(modelId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,modelId)));

        Set<ImageFile> images = model.getImage();

        // Resim setini bir liste olarak dönüştürün
        List<ImageFile> imageList = new ArrayList<>(images);

        return imageList;

    }
}
