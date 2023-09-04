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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


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

        ProductResponse productResponse = productMapper.productToProductResponse(product);
        productResponse.setBrandId(product.getBrand().getId());
        productResponse.setCategoryId(product.getCategory().getId());
        return productResponse;

    }

    public ProductResponse updateProductById(Long id, ProductRequest productRequest) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION));


        if (product.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        product.setTitle(productRequest.getTitle());
        product.setShortDescription(productRequest.getShortDescription());
        product.setLongDescription(productRequest.getLongDescription());
        product.setIsFeatured(productRequest.getIsFeatured());
        product.setIsNew(productRequest.getIsNew());
        product.setIsActive(productRequest.getIsActive());
        product.setBrand(brandService.findBrandById(productRequest.getBrandId()));
        product.setCategory(categoryService.findCategoryById(productRequest.getCategoryId()));
        // product.setImage(productRequest.getImage());
        product.setUpdatedAt(LocalDateTime.now());
        product.setSequence(productRequest.getSequence());
        product.setBuiltIn(productRequest.getBuiltIn());

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

        ProductPropertyKeyResponse productPropertyKeyResponse = productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyResponce(productPropertyKey);
        productPropertyKeyResponse.setProductId(product.getId());

        return productPropertyKeyResponse;
    }

    public ProductResponse getProductById(Long id) {

            Product product = productRepository.findById(id).orElseThrow(()->
                    new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION));

            return productMapper.productToProductResponse(product);

    }

    public List<ProductResponse> getAllProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            List<Product> productList = productRepository.findAll();

            return productMapper.productsToProductResponses(productList);

        } else {

            List<Product> productList = (productRepository.findByIsActive(true));
            List<Product> filteredProducts = productList.stream()
                    .filter(p -> p.getBrand().getIsActive() && p.getCategory().getIsActive())
                    .collect(Collectors.toList());

            return productMapper.productsToProductResponses(filteredProducts);


        }

    }

        public ProductResponse deleteProductById (Long id){

            Product product = productRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION));


            if (product.getBuiltIn()) {
                throw new BadRequestException(String.format(ErrorMessage.PRODUCT_CANNOT_DELETE_EXCEPTION, id));
            }

            //TODO: Offer_Item Ürün varmı yoksa exp.

            productRepository.deleteById(id);

            return productMapper.productToProductResponse(product);


        }
    }