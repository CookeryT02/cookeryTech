package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.ProductObjectResponse;
import com.tpe.cookerytech.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface ProductMapper{


    Product productRequestToProduct(ProductRequest productRequest);

    ProductResponse productToProductResponse(Product product);


    default List<ProductResponse> productsToProductResponses(List<Product> products) {

        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setTitle(product.getTitle());
            response.setShortDescription(product.getShortDescription());
            response.setLongDescription(product.getLongDescription());
            response.setIsFeatured(product.getIsFeatured());
            response.setIsNew(product.getIsNew());
            response.setIsActive(product.getIsActive());
            response.setSequence(product.getSequence());
            response.setCreatedAt(product.getCreatedAt());
            response.setUpdatedAt(product.getUpdatedAt());
            response.setBrandId(product.getBrand().getId());
            response.setCategoryId(product.getCategory().getId());
            productResponses.add(response);
        }
        return productResponses;
    }
    default Page<ProductResponse> productsToProductResponseCustomer(List<Product> filteredProductsCustomer, Pageable pageable){

        List<ProductResponse> productResponse = filteredProductsCustomer.stream().map(this::productToProductResponse).collect(Collectors.toList());

        return new PageImpl<>(productResponse,pageable,productResponse.size());
    }

    default Page<ProductResponse> productsToProductResponseTrue(List<Product> filteredProductsTrue, Pageable pageable){

        List<ProductResponse> productResponse = filteredProductsTrue.stream().map(this::productToProductResponse).collect(Collectors.toList());

        return new PageImpl<>(productResponse,pageable,productResponse.size());
    }

    List<ProductObjectResponse> productsToProductObjectResponses(List<Product> productList);

    ProductObjectResponse productToProductObjectResponse(Product product);

}
