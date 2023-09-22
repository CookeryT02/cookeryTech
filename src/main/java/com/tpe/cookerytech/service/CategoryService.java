package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
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
import java.util.List;


@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;



    public CategoryService(CategoryMapper categoryMapper, CategoryRepository categoryRepository, ProductMapper productMapper, ProductRepository productRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }



    //B01
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



    //B02
    public CategoryResponse getOneCategory(Long categoryId) {

        Category category = findCategoryById(categoryId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isProductManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PRODUCT_MANAGER"));

        if (!isAdmin && !category.getIsActive()) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION, categoryId));
        }
        return categoryMapper.categoryToCategoryResponse(category);
    }




    //B03
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



    //B04
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



    //B05
    public CategoryResponse removeCategoryById(Long id) {

        Category category = findCategoryById(id);
        CategoryResponse categoryResponse = categoryMapper.categoryToCategoryResponse(category);

        //Built-in
        if (category.getBuilt_in()) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION, id));
        }

        if(!productRepository.findByCategoryId(id).isEmpty()){
            throw new BadRequestException(String.format(ErrorMessage.CATEGORY_CANNOT_DELETE_EXCEPTION, id));
        }
        categoryRepository.delete(category);

        return categoryResponse;
    }




    //B06
    public List<ProductResponse> getActiveProductsByCategoryId(Long id) {

        List<Product> productIdCategory = productRepository.findByCategoryIdAndIsActiveTrue(id);

        return productMapper.productsToProductResponses(productIdCategory);
    }




    //************************************* Helper Methods **********************************************

    public boolean isTitleUnique(String title) {
        return categoryRepository.existsByTitle(title);
    }

    private String generateSlugFromTitle(String title) {
        // Türkçe karakterleri çıkartma işlemi
        String normalizedTitle = removeTurkishCharacters(title);

        // Diğer özel karakterleri çıkartma işlemi
        String cleanedText = normalizedTitle.replaceAll("[^a-zA-Z0-9 -]", "");

        // Boşlukları '-' ile değiştirme
        String slug = cleanedText.replaceAll(" ", "-");

        return slug.toLowerCase();
    }

    private Category getCategory(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id)));

        return category;

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
}

