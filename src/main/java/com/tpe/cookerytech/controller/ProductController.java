package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.ProductPropertyKeyRequest;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.ProductPropertyKeyResponse;
import com.tpe.cookerytech.dto.response.ProductResponse;
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
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    private final BrandService brandService;

    private final CategoryService categoryService;

    public ProductController(ProductService productService, BrandService brandService, CategoryService categoryService, BrandService brandService1, CategoryService categoryService1) {
        this.productService = productService;
        this.brandService = brandService1;
        this.categoryService = categoryService1;
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

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<Page<ProductResponse>> getAllProductsWithPage(
            @RequestParam(required = false,defaultValue = "",name = "q") String q,
            @RequestParam(required = false,defaultValue = "brandId") Long brandId,
            @RequestParam(required = false,defaultValue = "categoryId") Long categoryId,
            @RequestParam(required = false,defaultValue = "0",name = "page") int page,
            @RequestParam(required = false,defaultValue = "20",name = "size") int size,
            @RequestParam(required = false,defaultValue = "create_at",name = "sort") String sort,
            @RequestParam(required = false,defaultValue = "DESC",name = "type") String type){


        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(type), sort));

        Page<ProductResponse> productResponse = productService.allProducts(q,brandId,categoryId,pageable);

        return ResponseEntity.ok(productResponse);



    }


    //A02
}
