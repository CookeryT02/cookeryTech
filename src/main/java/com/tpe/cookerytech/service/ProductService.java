package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.domain.Category;
import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.mapper.ProductMapper;
import com.tpe.cookerytech.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final BrandService brandService;

    private final CategoryService categoryService;


    public ProductService(ProductRepository productRepository, ProductMapper productMapper, BrandService brandService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    public ProductResponse createProducts(ProductRequest productRequest) {

        Brand brand = brandService.findBrandById(productRequest.getBrandId());

        Category category = categoryService.findCategoryById(productRequest.getCategoryId());


        Product product = productMapper.productRequestToProduct(productRequest);

        product.setBrand(brand);
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(null);
        product.setBuiltIn(false);
        product.setSlug("null");

        productRepository.save(product);

        return productMapper.productToProductResponse(product);

    }



}
