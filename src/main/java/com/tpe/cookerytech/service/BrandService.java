package com.tpe.cookerytech.service;

import com.tpe.cookerytech.dto.domain.Brand;
import com.tpe.cookerytech.dto.request.BrandRequest;
import com.tpe.cookerytech.dto.response.BrandResponse;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ResourcesNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.BrandMapper;
import com.tpe.cookerytech.repository.BrandRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public BrandService(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    public BrandResponse createBrand(BrandRequest brandRequest) {

        Brand brand = brandMapper.brandRequestToBrand(brandRequest);

        brand.setCreateAt(LocalDateTime.now());
        brand.setUpdateAt(null);

        brandRepository.save(brand);

        return brandMapper.brandToBrandResponse(brand);

    }

    public BrandResponse getBrandById(Long id) {

        Brand brand = brandRepository.findById(id).orElseThrow(()->
                new ResourcesNotFoundException(ErrorMessage.BRAND_NOT_FOUND_EXCEPTION));

        return brandMapper.brandToBrandResponse(brand);
    }

    public BrandResponse updateBrandById(Long id, BrandRequest brandRequest) {

        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new ResourcesNotFoundException(ErrorMessage.BRAND_NOT_FOUND_EXCEPTION));

        if (brand.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        brand.setName(brandRequest.getName());
        brand.setProfitRate(brandRequest.getProfitRate());
        brand.setUpdateAt(LocalDateTime.now());
        brand.setIsActive(brandRequest.getIsActive());
        brand.setBuiltIn(brandRequest.getBuiltIn());

        brandRepository.save(brand);

        return brandMapper.brandToBrandResponse(brand);
    }
}
