package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.BrandRequest;
import com.tpe.cookerytech.dto.response.BrandResponse;
import com.tpe.cookerytech.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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



    //C01 -> It should return brands   Page:48
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<Page<BrandResponse>> getAllBrandsWithPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value = "type",
                    required = false,defaultValue = "DESC")Sort.Direction direction
    ){
        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));

        Page<BrandResponse> brandResponses= brandService.findAllWithPage(pageable);

        return ResponseEntity.ok(brandResponses);
    }




    //C02 -> It should return a brand
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandResponse> getBrandsById(@Valid @PathVariable Long id){

        BrandResponse brandResponse = brandService.getBrandById(id);

        return ResponseEntity.ok(brandResponse);
    }




    //C03 -> It should create a brand
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandResponse> createBrands(@Valid @RequestBody BrandRequest brandRequest){

        BrandResponse brandResponse = brandService.createBrand(brandRequest);

        return ResponseEntity.ok(brandResponse);
    }




    //C04 -> It should update the brand
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandResponse> updateBrand(@PathVariable Long id,
                                                     @Valid @RequestBody BrandRequest brandRequest){

        BrandResponse brandResponse = brandService.updateBrandById(id, brandRequest);

        return ResponseEntity.ok(brandResponse);
    }




    //C05 -> It should delete the brand
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandResponse> deleteBrandById(@PathVariable Long id){

        BrandResponse brandResponse = brandService.deleteBrandById(id);

        return ResponseEntity.ok(brandResponse);
    }
}
