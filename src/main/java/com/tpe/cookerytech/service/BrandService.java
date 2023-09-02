package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.dto.request.BrandRequest;
import com.tpe.cookerytech.dto.response.BrandResponse;
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

    public Brand findBrandById(Long brandId) {

        Brand brand = brandRepository.findById(brandId).orElseThrow(()->
                new ResourcesNotFoundException(ErrorMessage.BRAND_NOT_FOUND_EXCEPTION));

        return brand;

    }
}
