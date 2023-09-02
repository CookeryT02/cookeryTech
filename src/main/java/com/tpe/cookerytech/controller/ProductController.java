package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.service.BrandService;
import com.tpe.cookerytech.service.CategoryService;
import com.tpe.cookerytech.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService, BrandService brandService, CategoryService categoryService) {
        this.productService = productService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest){

        ProductResponse productResponse = productService.createProducts(productRequest);

        return ResponseEntity.ok(productResponse);

    }




}
