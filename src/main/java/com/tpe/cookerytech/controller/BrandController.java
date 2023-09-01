package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.BrandRequest;
import com.tpe.cookerytech.dto.response.BrandResponse;
import com.tpe.cookerytech.service.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandResponse> createBrands(@Valid @RequestBody BrandRequest brandRequest){

        BrandResponse brandResponse = brandService.createBrand(brandRequest);

        return ResponseEntity.ok(brandResponse);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandResponse> getBrandsById(@Valid @RequestParam Long id){

        BrandResponse brandResponse = brandService.getBrandById(id);

        return ResponseEntity.ok(brandResponse);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandResponse> updateBrand(@PathVariable Long id,
                                                     @Valid @RequestBody BrandRequest brandRequest){

        BrandResponse brandResponse = brandService.updateBrandById(id, brandRequest);

        return ResponseEntity.ok(brandResponse);
    }


    //DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandResponse> deleteBrandById(@PathVariable Long id){

        BrandResponse brandResponse = brandService.deleteBrandById(id);

        return ResponseEntity.ok(brandResponse);

    }

}
