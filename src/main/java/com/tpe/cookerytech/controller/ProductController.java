package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.dto.request.*;
import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.BrandResponse;
import com.tpe.cookerytech.dto.response.ModelResponse;
import com.tpe.cookerytech.dto.response.ProductPropertyKeyResponse;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.service.BrandService;
import com.tpe.cookerytech.service.CategoryService;
import com.tpe.cookerytech.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService, BrandService brandService, CategoryService categoryService) {
        this.productService = productService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {

        ProductResponse productResponse = productService.createProducts(productRequest);

        return ResponseEntity.ok(productResponse);

    }

    @DeleteMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductResponse> deleteProductById(@PathVariable Long id){

        ProductResponse productResponse = productService.deleteProductById(id);

        return ResponseEntity.ok(productResponse);

    }

    @PutMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @RequestBody ProductRequest productRequest) {

        ProductResponse productResponse = productService.updateProductById(id, productRequest);

        return ResponseEntity.ok(productResponse);
    }

    @PostMapping("/properties")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductPropertyKeyResponse> createPPKey(@Valid @RequestBody ProductPropertyKeyRequest productPropertyKeyRequest){

        ProductPropertyKeyResponse productPropertyKeyResponse = productService.createPPKey(productPropertyKeyRequest);

        return ResponseEntity.ok(productPropertyKeyResponse);
    }

    @GetMapping("/featured")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){

        ProductResponse productResponse=productService.getProductById(id);

        return ResponseEntity.ok(productResponse);

    }

    @DeleteMapping("/properties/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductPropertyKeyResponse> deletePPK(@PathVariable Long id){

        ProductPropertyKeyResponse productPropertyKeyResponse = productService.deletePPKById(id);

        return ResponseEntity.ok(productPropertyKeyResponse);
    }


    @PostMapping("/models")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ModelResponse> createProductModel(@Valid @RequestBody ModelRequest modelRequest) {

        ModelResponse modelResponse = productService.createProductModels(modelRequest);

        return ResponseEntity.ok(modelResponse);

    }

    @PutMapping("/models/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ModelResponse> updateProductModel(@PathVariable Long id,
                                                            @Valid @RequestBody ModelRequest modelRequest){

        ModelResponse modelResponse = productService.updateProductModelById(id,modelRequest);

        return ResponseEntity.ok(modelResponse);

    }

}
