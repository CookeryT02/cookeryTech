package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.CategoryRequest;
import com.tpe.cookerytech.dto.response.CategoryResponse;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.service.CategoryService;
import com.tpe.cookerytech.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {


    private final CategoryService categoryService;

    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {

        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);

        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<CategoryResponse>> getAllCategory(){

        List<CategoryResponse> allCategory = categoryService.getAllCategory();

        return ResponseEntity.ok(allCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long id) {

        CategoryResponse categoryResponse = categoryService.removeCategoryById(id);

        return ResponseEntity.ok(categoryResponse);

    }




    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryResponse> updateCategory( @PathVariable Long id,
                                                      @Valid @RequestBody CategoryRequest categoryRequest ){

        CategoryResponse categoryResponse = categoryService.updateCategory( id, categoryRequest);

        return  ResponseEntity.ok(categoryResponse);


    }



}
