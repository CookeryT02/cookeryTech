package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.domain.ProductPropertyKey;
import com.tpe.cookerytech.dto.request.ProductPropertyKeyRequest;
import com.tpe.cookerytech.dto.response.ProductPropertyKeyResponse;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductPropertyKeyMapper {


    ProductPropertyKey productPropertyKeyRequestToProductPropertyKey(ProductPropertyKeyRequest productPropertyKeyRequest);

    ProductPropertyKeyResponse productPropertyKeyToProductPropertyKeyResponse(ProductPropertyKey productPropertyKey);

    default List<ProductPropertyKeyResponse> ppkListToPPKResponseList(List<ProductPropertyKey> ppkList){
        List<ProductPropertyKeyResponse> ppkResponseList = new ArrayList<>();
        for (ProductPropertyKey ppk : ppkList){
            ProductPropertyKeyResponse ppkResponse = productPropertyKeyToProductPropertyKeyResponse(ppk);
            ppkResponse.setProductId(ppk.getProduct().getId());
            ppkResponseList.add(ppkResponse);
        }
        return ppkResponseList;
    }
}