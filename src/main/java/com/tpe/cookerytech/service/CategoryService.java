package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Category;
import com.tpe.cookerytech.dto.request.CategoryRequest;
import com.tpe.cookerytech.dto.response.CategoryResponse;
import com.tpe.cookerytech.exception.ConflictException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.CategoryMapper;
import com.tpe.cookerytech.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;


    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = categoryMapper.categoryRequestToCategory(categoryRequest);

//        if (isCategoryExist(categoryRequest.getTitle())) {
//            throw new ConflictException(String.format(ErrorMessage.CATEGORY_ALREADY_EXIST_EXCEPTION, category.getName()));
//        }
        category.setCreateAt(LocalDateTime.now());
        category.setUpdateAt(null);
        categoryRepository.save(category);


        return categoryMapper.categoryToCategoryResponse(category);
    }
//    private boolean isCategoryExist(String categoryName) {
//
//        return categoryRepository.existsBy(categoryName);
//
//    }
}
