package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.domain.Category;
import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.domain.ProductPropertyKey;
import com.tpe.cookerytech.dto.request.ProductPropertyKeyRequest;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.ProductPropertyKeyResponse;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.ProductMapper;
import com.tpe.cookerytech.mapper.ProductPropertyKeyMapper;
import com.tpe.cookerytech.repository.ProductPropertyKeyRepository;
import com.tpe.cookerytech.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final BrandService brandService;

    private final CategoryService categoryService;

    private final ProductPropertyKeyMapper productPropertyKeyMapper;

    private final ProductPropertyKeyRepository productPropertyKeyRepository;




    public ProductService(ProductRepository productRepository, ProductMapper productMapper, BrandService brandService, CategoryService categoryService, ProductPropertyKeyMapper productPropertyKeyMapper, ProductPropertyKeyRepository productPropertyKeyRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.productPropertyKeyMapper = productPropertyKeyMapper;
        this.productPropertyKeyRepository = productPropertyKeyRepository;
    }

    public ProductResponse createProducts(ProductRequest productRequest) {

        Brand brand = brandService.findBrandById(productRequest.getBrandId());

        Category category = categoryService.findCategoryById(productRequest.getCategoryId());


        Product product = productMapper.productRequestToProduct(productRequest);

        product.setBrand(brand);
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(null);
        product.setBuiltIn(false);
        product.setSlug("null");

        productRepository.save(product);

        return productMapper.productToProductResponse(product);

    }

    public ProductResponse updateProductById(Long id, ProductRequest productRequest) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION));


        if (product.isBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        product.setTitle(productRequest.getTitle());
        product.setShortDescription(productRequest.getShortDescription());
        product.setLongDescription(productRequest.getLongDescription());
        product.setFeatured(productRequest.isFeatured());
        product.setNew(productRequest.isNew());
        product.setActive(productRequest.isActive());
        product.setBrand(brandService.findBrandById(productRequest.getBrandId()));
        product.setCategory(categoryService.findCategoryById(productRequest.getCategoryId()));
        // product.setImage(productRequest.getImage());
        product.setUpdatedAt(LocalDateTime.now());
        product.setSequence(productRequest.getSequence());
        product.setBuiltIn(productRequest.isBuiltIn());

        productRepository.save(product);

        ProductResponse productResponse = productMapper.productToProductResponse(product);
        productResponse.setBrandId(product.getBrand().getId());
        productResponse.setCategoryId(product.getCategory().getId());
        return productResponse;
    }

    public ProductPropertyKeyResponse createPPKey(ProductPropertyKeyRequest productPropertyKeyRequest) {

        ProductPropertyKey productPropertyKey = productPropertyKeyMapper.productPropertyKeyRequestToProductPropertyKey(productPropertyKeyRequest);

        Product product = productRepository.findById(productPropertyKeyRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION));

        productPropertyKey.setProduct(product);
        productPropertyKeyRepository.save(productPropertyKey);

        ProductPropertyKeyResponse productPropertyKeyResponse =productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyResponce(productPropertyKey);
        productPropertyKeyResponse.setProductId(product.getId());

        return productPropertyKeyResponse;
    }
}
