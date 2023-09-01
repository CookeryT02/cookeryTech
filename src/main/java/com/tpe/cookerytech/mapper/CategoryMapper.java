package com.tpe.cookerytech.mapper;


import com.tpe.cookerytech.dto.domain.Category;
import com.tpe.cookerytech.dto.request.CategoryRequest;
import com.tpe.cookerytech.dto.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {


    @Mapping(target = "id", ignore = true)
    Category categoryRequestToCategory(CategoryRequest categoryRequest);

    CategoryResponse categoryToCategoryResponse(Category category);
}
