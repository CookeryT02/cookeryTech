package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.domain.enums.RoleType;
import com.tpe.cookerytech.dto.request.CategoryRequest;
import com.tpe.cookerytech.dto.response.CategoryResponse;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ConflictException;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.CategoryMapper;
import com.tpe.cookerytech.mapper.ProductMapper;
import com.tpe.cookerytech.repository.CategoryRepository;
import com.tpe.cookerytech.repository.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    private final ProductRepository productRepository;

    private final UserService userService;


    public CategoryService(CategoryMapper categoryMapper, CategoryRepository categoryRepository, ProductMapper productMapper, ProductRepository productRepository, UserService userService) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {


        Category category = categoryMapper.categoryRequestToCategory(categoryRequest);

        if(isTitleUnique(category.getTitle())){
            throw new ConflictException(String.format(ErrorMessage.CATEGORY_ALREADY_EXIST_EXCEPTION, category.getTitle()));
        }

        String title=category.getTitle();
        String slug=  generateSlugFromTitle(title);
        category.setSlug(slug);
        category.setCreateAt(LocalDateTime.now());
        category.setUpdateAt(null);
        categoryRepository.save(category);



        return categoryMapper.categoryToCategoryResponse(category);
    }


    public boolean isTitleUnique(String title) {
        return categoryRepository.existsByTitle(title);
    }


    public CategoryResponse removeCategoryById(Long id) {

        Category category = findCategoryById(id);
        CategoryResponse categoryResponse = categoryMapper.categoryToCategoryResponse(category);

        //Built-in
        if (category.getBuilt_in()) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION, id));
        }

        // Category product var mı kontrol et !!!

//        if (!(category.getProductList().size() == 0)) {
//            throw new BadRequestException(String.format(ErrorMessage.CATEGORY_CANNOT_DELETE_EXCEPTION, id));
//        }


        categoryRepository.delete(category);

        return categoryResponse;
    }



    // YARDIMCI METHEDLAR
    private String generateSlugFromTitle(String title) {
        // Türkçe karakterleri çıkartma işlemi
        String normalizedTitle = removeTurkishCharacters(title);

        // Diğer özel karakterleri çıkartma işlemi
        String cleanedText = normalizedTitle.replaceAll("[^a-zA-Z0-9 -]", "");

        // Boşlukları '-' ile değiştirme
        String slug = cleanedText.replaceAll(" ", "-");

        return slug.toLowerCase();
    }




    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {

        Category category = getCategory(id);

        if(category.getBuilt_in()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }


        category.setTitle(categoryRequest.getTitle());
        category.setDescription(categoryRequest.getDescription());
        category.setSeq(categoryRequest.getSeq());
        category.setSlug(categoryRequest.getSlug());
        category.setIsActive(categoryRequest.getIsActive());
        category.setUpdateAt(LocalDateTime.now());

        CategoryResponse categoryResponse = categoryMapper.categoryToCategoryResponse(category);

        categoryRepository.save(category);

        return categoryResponse;
    }

    private Category getCategory(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id)));

        return category;

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
                new ResourceNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION));

        return category;
    }


    private String removeTurkishCharacters(String input) {
        if (input == null) {
            return null;
        }

        input = input.replaceAll("ı", "i").replaceAll("İ", "I")
                .replaceAll("ğ", "g").replaceAll("Ğ", "G")
                .replaceAll("ş", "s").replaceAll("Ş", "S")
                .replaceAll("ç", "c").replaceAll("Ç", "C")
                .replaceAll("ö", "o").replaceAll("Ö", "O")
                .replaceAll("ü", "u").replaceAll("Ü", "U");

        return input;
    }


    public List<ProductResponse> getActiveProductsByCategoryId(Long id) {

        List<Product> productIdCategory = productRepository.findByCategoryIdAndIsActiveTrue(id);

        return productMapper.productsToProductResponses(productIdCategory);


    }
}

