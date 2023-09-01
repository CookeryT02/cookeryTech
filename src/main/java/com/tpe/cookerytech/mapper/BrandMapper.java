package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.dto.domain.Brand;
import com.tpe.cookerytech.dto.request.BrandRequest;
import com.tpe.cookerytech.dto.response.BrandResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand brandRequestToBrand(BrandRequest brandRequest);

    BrandResponse brandToBrandResponse(Brand brand);
}

