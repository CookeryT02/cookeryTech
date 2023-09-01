package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.CategoryRequest;
import com.tpe.cookerytech.dto.response.CategoryResponse;
import com.tpe.cookerytech.dto.response.CkResponse;
import com.tpe.cookerytech.dto.response.ResponseMessage;
import com.tpe.cookerytech.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {


    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {

        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);

        return ResponseEntity.ok(categoryResponse);
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

       // CkResponse response = new CkResponse(ResponseMessage.CATEGORY_UPDATED_RESPONSE_MESSAGE,true);

        return  ResponseEntity.ok(categoryResponse);


    }








}
