package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product productRequestToProduct(ProductRequest productRequest);

    ProductResponse productToProductResponse(Product product);


}
