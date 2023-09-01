package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Category;
import com.tpe.cookerytech.dto.request.CategoryRequest;
import com.tpe.cookerytech.dto.response.CategoryResponse;
import com.tpe.cookerytech.exception.ConflictException;
import com.tpe.cookerytech.exception.ResourcesNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.CategoryMapper;
import com.tpe.cookerytech.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
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
        String title=category.getTitle();
        String slug=  generateSlugFromTitle(title);
        category.setSlag(slug);
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



    public CategoryResponse removeCategoryById(Long id) {

        Category category = findCategoryById(id);
        CategoryResponse categoryResponse = categoryMapper.categoryToCategoryResponse(category);

        //Built-in
        if (category.getBuilt_in()) {
            throw new ResourcesNotFoundException(String.format(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION, id));
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
    public Category findCategoryById(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourcesNotFoundException(String.format(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION, id)));

        return category;
    }
}

