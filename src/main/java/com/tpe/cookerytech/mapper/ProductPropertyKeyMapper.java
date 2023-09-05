package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.domain.ProductPropertyKey;
import com.tpe.cookerytech.dto.request.ProductPropertyKeyRequest;
import com.tpe.cookerytech.dto.response.ProductPropertyKeyResponse;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ProductPropertyKeyMapper {


    ProductPropertyKey productPropertyKeyRequestToProductPropertyKey(ProductPropertyKeyRequest productPropertyKeyRequest);

    ProductPropertyKeyResponse productPropertyKeyToProductPropertyKeyResponce(ProductPropertyKey productPropertyKey);
}