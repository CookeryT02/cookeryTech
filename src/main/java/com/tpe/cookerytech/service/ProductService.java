package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.dto.request.ModelRequest;
import com.tpe.cookerytech.dto.request.ProductPropertyKeyRequest;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.ModelResponse;
import com.tpe.cookerytech.dto.response.ProductObjectResponse;
import com.tpe.cookerytech.dto.response.ProductPropertyKeyResponse;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.*;
import com.tpe.cookerytech.repository.CurrencyRepository;
import com.tpe.cookerytech.repository.ModelRepository;
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
    private final CurrencyRepository currencyRepository;
    private final ModelMapper modelMapper;

    private final ProductPropertyKeyMapper productPropertyKeyMapper;

    private final ProductPropertyKeyRepository productPropertyKeyRepository;
    private final ModelRepository modelRepository;
    private final BrandMapper brandMapper;

    private final CategoryMapper categoryMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, BrandService brandService, CategoryService categoryService, CurrencyService currencyService, CurrencyRepository currencyRepository, ModelMapper modelMapper, ProductPropertyKeyMapper productPropertyKeyMapper, ProductPropertyKeyRepository productPropertyKeyRepository, ModelRepository modelRepository, BrandMapper brandMapper, CategoryMapper categoryMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.currencyRepository = currencyRepository;
        this.modelMapper = modelMapper;
        this.productPropertyKeyMapper = productPropertyKeyMapper;
        this.productPropertyKeyRepository = productPropertyKeyRepository;
        this.modelRepository = modelRepository;
        this.brandMapper = brandMapper;
        this.categoryMapper = categoryMapper;
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

    public List<ProductObjectResponse> getAllProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            List<Product> productList = productRepository.findAll();

            List<ProductObjectResponse> listObjectResponse=productMapper.productsToProductObjectResponses(productList);


            return listObjectResponse;

        } else {

            List<Product> productList = (productRepository.findByIsActive(true));
            List<Product> filteredProducts =productList.stream()
                    .filter(p -> {
                        Brand brand = p.getBrand();
                        Category category = p.getCategory();
                        System.out.println(p.getCategory().getIsActive());
                        return p.getIsFeatured() && brand != null && category != null && brand.getIsActive() && category.getIsActive();
                    })
                    .collect(Collectors.toList());

            return productMapper.productsToProductObjectResponses(filteredProducts);

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





    public ProductPropertyKeyResponse deletePPKById(Long id) {
        ProductPropertyKey productPropertyKey = productPropertyKeyRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.PRODUCT_PROPERTY_KEY_NOT_FOUND));

        if (productPropertyKey.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        } //else if () {
            //model value degeri varsa silinemez eklenecek
      //  }
        else {
            productPropertyKeyRepository.deleteById(id);
        }
        return productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyResponce(productPropertyKey);
    }

    public ModelResponse createProductModels(ModelRequest modelRequest) {

        Product product= productRepository.findById(modelRequest.getProductId()).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION));

        Currency currency = currencyRepository.findById(modelRequest.getCurrencyId()).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION));

        Model model = modelMapper.modelRequestToModel(modelRequest);

        model.setProduct(product);
        model.setCurrency(currency);
        model.setCreate_at(LocalDateTime.now());
        modelRepository.save(model);
        ModelResponse modelResponse=modelMapper.modelToModelResponse(model);
        modelResponse.setProductId(product.getId());
        modelResponse.setCurrencyId(currency.getId());
        return modelResponse;
    }
}