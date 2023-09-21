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


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }



    //B01 -> It should return categories  Page:41
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){

        List<CategoryResponse> categoryResponses = categoryService.getAllCategory();

        return ResponseEntity.ok(categoryResponses);
    }




    //B02 -> It will return a category
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryResponse> getOneCategory(@PathVariable Long id){

        CategoryResponse  Category = categoryService.getOneCategory(id);

        return ResponseEntity.ok(Category);
    }



    //B03 -> It will create a category
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {

        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);

        return ResponseEntity.ok(categoryResponse);
    }



    //B04 -> It will update the category
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryResponse> updateCategory( @PathVariable Long id,
                                                            @Valid @RequestBody CategoryRequest categoryRequest ){

        CategoryResponse categoryResponse = categoryService.updateCategory( id, categoryRequest);

        return  ResponseEntity.ok(categoryResponse);
    }



    //B05 -> It will delete the category
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long id) {

        CategoryResponse categoryResponse = categoryService.removeCategoryById(id);

        return ResponseEntity.ok(categoryResponse);
    }




    //B06
    @GetMapping("/{id}/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<ProductResponse>> getActiveProductsByCategoryId(@PathVariable Long id) {
        List<ProductResponse> activeProducts = categoryService.getActiveProductsByCategoryId(id);
        return ResponseEntity.ok(activeProducts);
    }




























}
