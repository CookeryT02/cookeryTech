package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.dto.request.BrandRequest;
import com.tpe.cookerytech.dto.response.BrandResponse;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.BrandMapper;
import com.tpe.cookerytech.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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



    //C01
    public Page<BrandResponse> findAllWithPage(Pageable pageable) {

        Page<Brand> brandPage= brandRepository.findAll(pageable);

        return brandPage.map(brandMapper::brandToBrandResponse);
    }




    //C02
    public BrandResponse getBrandById(Long id) {

        Brand brand = brandRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.BRAND_NOT_FOUND_EXCEPTION));

        return brandMapper.brandToBrandResponse(brand);
    }



    //C03
    public BrandResponse createBrand(BrandRequest brandRequest) {

       Brand brand = brandMapper.brandRequestToBrand(brandRequest);

       brand.setCreateAt(LocalDateTime.now());
       brand.setUpdateAt(null);

       brandRepository.save(brand);

       return brandMapper.brandToBrandResponse(brand);
    }



    //C04
    public BrandResponse updateBrandById(Long id, BrandRequest brandRequest) {

        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.BRAND_NOT_FOUND_EXCEPTION));

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




    //C05
    public BrandResponse deleteBrandById(Long id) {

        Brand brand = brandRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.BRAND_NOT_FOUND_EXCEPTION));

        if (brand.getBuiltIn()){
            throw  new BadRequestException(String.format(ErrorMessage.BRAND_CANNOT_DELETE_EXCEPTION,id));
        }

        ///////Checking Products///////
//        Product product= productRepository.findByBrandId(id);
//
//         if(product.getBrand().getId()==null){
//           brandRepository.deleteBrandById(brand);
//         } else {
//             throw new BadRequestException(String.format(ErrorMessage.BRAND_CANNOT_DELETE_EXCEPTION,id));
//         }

        brandRepository.deleteById(id);

        return brandMapper.brandToBrandResponse(brand);
    }




    //************************************ HELPER METHODS ***************************************
    public Brand findBrandById(Long brandId) {

        Brand brand = brandRepository.findById(brandId).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.BRAND_NOT_FOUND_EXCEPTION));

        return brand;
    }
}
