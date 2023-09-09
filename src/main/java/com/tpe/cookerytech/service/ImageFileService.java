package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.ImageFile;
import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.repository.ImageFileRepository;
import com.tpe.cookerytech.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    private final ProductRepository productRepository;

    public ImageFileService(ImageFileRepository imageFileRepository, ProductRepository productRepository) {
        this.imageFileRepository = imageFileRepository;
        this.productRepository = productRepository;
    }


    public List<ImageFile> getProductImages(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,productId)));

        Set<ImageFile> images = product.getImage();

        // Resim setini bir liste olarak dönüştürün
        List<ImageFile> imageList = new ArrayList<>(images);

        return imageList;

    }
}
