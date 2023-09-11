package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.dto.request.*;
import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.*;
import com.tpe.cookerytech.service.BrandService;
import com.tpe.cookerytech.service.CategoryService;
import com.tpe.cookerytech.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
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
    public ResponseEntity<List<ProductObjectResponse>> getAllProducts() {
        List<ProductObjectResponse> allProducts = productService.getAllProducts();
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

    @DeleteMapping("/models/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ModelResponse> deleteProductModel(@PathVariable Long id){

        ModelResponse modelResponse = productService.deleteModelById(id);

        return ResponseEntity.ok(modelResponse);
    }

    @PutMapping("/properties/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductPropertyKeyResponse> updatePPKeyById(@PathVariable Long id,
                                                                      @Valid @RequestBody ProductPropertyKeyRequest productPropertyKeyRequest){

        ProductPropertyKeyResponse productPropertyKeyResponse = productService.updatePPKeyById(id, productPropertyKeyRequest);

        return ResponseEntity.ok(productPropertyKeyResponse);

    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<Page<ProductResponse>> getAllProductsWithPage(
            @RequestParam(required = false,defaultValue = "",name = "q") String q,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false,defaultValue = "0",name = "page") int page,
            @RequestParam(required = false,defaultValue = "20",name = "size") int size,
            @RequestParam(required = false,defaultValue = "id",name = "sort") String sort,
            @RequestParam(required = false,defaultValue = "DESC",name = "type") String type){


        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(type), sort));

        Page<ProductResponse> productResponse = productService.allProducts(q,pageable,brandId,categoryId);

        return ResponseEntity.ok(productResponse);



    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ModelResponse>> getProductsByIdModels(@PathVariable Long id) {


        List<ModelResponse> modelResponseList  = productService.listProductsByIdModels(id);

        return ResponseEntity.ok(modelResponseList);

    }

    @GetMapping("/{id}/properties")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ProductPropertyKeyResponse>> getPPKByProductId(@PathVariable Long id){


         List<ProductPropertyKeyResponse> ppkResponseList = productService.listPPKeysByProductId(id);

        return ResponseEntity.ok(ppkResponseList);
    }

    //*****************************Yardimci Method**************************************************

    @GetMapping("/{id}/models")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ModelResponse>> getModelsByProductId(@PathVariable Long id){


        List<ModelResponse> modelResponseList = productService.getModelsByProductId(id);

        return ResponseEntity.ok(modelResponseList);
    }

}
