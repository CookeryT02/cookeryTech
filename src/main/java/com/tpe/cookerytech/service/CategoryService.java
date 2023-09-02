package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.domain.Category;
import com.tpe.cookerytech.domain.Role;
import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.domain.enums.RoleType;
import com.tpe.cookerytech.dto.request.CategoryRequest;
import com.tpe.cookerytech.dto.response.CategoryResponse;
import com.tpe.cookerytech.exception.ResourcesNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.CategoryMapper;
import com.tpe.cookerytech.repository.CategoryRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;


    private final CategoryRepository categoryRepository;

    private final UserService userService;


    public CategoryService(CategoryMapper categoryMapper, CategoryRepository categoryRepository, UserService userService) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
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


    public List<CategoryResponse> getAllCategory() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            List<Category> categories = categoryRepository.findAll();

            List<CategoryResponse> categoryResponses = categoryMapper.map(categories);

            return categoryResponses;

        } else {

            List<Category> categories = categoryRepository.findByIsActive(true);

            List<CategoryResponse> categoryResponses = categoryMapper.map(categories);

            return categoryResponses;


        }

    }

    public Category findCategoryById(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                new ResourcesNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION));

        return category;
    }
}
