package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.mapper.ProductMapper;
import com.tpe.cookerytech.repository.ProductRepository;
import org.springframework.stereotype.Service;



@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;


    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse createProducts(ProductRequest productRequest) {

//        Product product = productMapper.productRequestToProduct(productRequest);
//
//        product.setCreatedAt(LocalDateTime.now());
//        product.setUpdatedAt(null);
//
//        productRepository.save(product);
//
//        return productMapper.productToProductResponse(product);

        return null;

    }
}
