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
import com.tpe.cookerytech.exception.ConflictException;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.*;
import com.tpe.cookerytech.repository.CurrencyRepository;
import com.tpe.cookerytech.repository.ModelRepository;
import com.tpe.cookerytech.repository.ProductPropertyKeyRepository;
import com.tpe.cookerytech.repository.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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


    public ProductService(ProductRepository productRepository, ProductMapper productMapper, BrandService brandService, CategoryService categoryService, CurrencyService currencyService, CurrencyRepository currencyRepository, ModelMapper modelMapper, ProductPropertyKeyMapper productPropertyKeyMapper, ProductPropertyKeyRepository productPropertyKeyRepository, ModelRepository modelRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.currencyRepository = currencyRepository;
        this.modelMapper = modelMapper;
        this.productPropertyKeyMapper = productPropertyKeyMapper;
        this.productPropertyKeyRepository = productPropertyKeyRepository;
        this.modelRepository = modelRepository;
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

        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));


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
        Product product = productRepository.findById(productPropertyKeyRequest.getProductId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,productPropertyKeyRequest.getProductId())));
        productPropertyKey.setProduct(product);

        List<ProductPropertyKey> productPropertyKeys = productPropertyKeyRepository.findByProductId(productPropertyKeyRequest.getProductId());
        for (ProductPropertyKey ppk:productPropertyKeys){
            if (ppk.getName().equals(productPropertyKeyRequest.getName())){
                throw  new ConflictException(String.format(ErrorMessage.PPK_ALREADY_EXIST_EXCEPTION,productPropertyKeyRequest.getName()));
            }
        }

        List<ModelResponse> modelResponseList = getModelsByProductId(productPropertyKey.getProduct().getId());
        for (ModelResponse modelResponse: modelResponseList
             ) { if (modelResponse.getTitle().equals(productPropertyKey.getName())){
                 throw new ConflictException(String.format(ErrorMessage.MODEL_ALREADY_EXIST_EXCEPTION,productPropertyKey.getName()));
        }
            }

        String[] modelFields = {"Title", "sku", "stock amount", "in box quantity", "seq", "buying price", "tax rate"};
        for (String w:modelFields){
           if (w.equalsIgnoreCase(productPropertyKeyRequest.getName())){
               throw new ConflictException(String.format(ErrorMessage.MODEL_FIELD_ALREADY_EXIST_EXCEPTION,productPropertyKeyRequest.getName()));
           }
        }

        productPropertyKeyRepository.save(productPropertyKey);

        ProductPropertyKeyResponse productPropertyKeyResponse = productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyResponse(productPropertyKey);
        productPropertyKeyResponse.setProductId(product.getId());



        return productPropertyKeyResponse;
    }

    public ProductResponse getProductById(Long id) {

            Product product = productRepository.findById(id).orElseThrow(()->
                    new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));

            ProductResponse productResponse = productMapper.productToProductResponse(product);
            productResponse.setBrandId(product.getBrand().getId());
            productResponse.setCategoryId(product.getCategory().getId());

            return productResponse;
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
                    new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));


            if (product.getBuiltIn()) {
                throw new BadRequestException(String.format(ErrorMessage.PRODUCT_CANNOT_DELETE_EXCEPTION, id));
            }

            //TODO: Offer_Item Ürün varmı yoksa exp.

            productRepository.deleteById(id);

            return productMapper.productToProductResponse(product);

    }





    public ProductPropertyKeyResponse deletePPKById(Long id) {
        ProductPropertyKey productPropertyKey = productPropertyKeyRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_PROPERTY_KEY_NOT_FOUND,id)));

        if (productPropertyKey.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        } //else if () {
            //model value degeri varsa silinemez eklenecek
      //  }
        else {
            productPropertyKeyRepository.deleteById(id);
        }
        ProductPropertyKeyResponse productPropertyKeyResponse = productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyResponse(productPropertyKey);
        productPropertyKeyResponse.setProductId(productPropertyKey.getProduct().getId());
        return productPropertyKeyResponse;
    }

    public ModelResponse createProductModels(ModelRequest modelRequest) {

        Product product= productRepository.findById(modelRequest.getProductId()).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,modelRequest.getProductId())));

        Currency currency = currencyRepository.findById(modelRequest.getCurrencyId()).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.CURRENCY_NOT_FOUND_EXCEPTION));

        Model model = modelMapper.modelRequestToModel(modelRequest);

        isSkuUnique(modelRequest.getSku());

        model.setSku(modelRequest.getSku());
        model.setProduct(product);
        model.setCurrency(currency);
        model.setCreate_at(LocalDateTime.now());
        modelRepository.save(model);
        ModelResponse modelResponse = modelMapper.modelToModelResponse(model);
        modelResponse.setProductId(product.getId());
        modelResponse.setCurrencyId(currency.getId());
        return modelResponse;
    }



    public ModelResponse updateProductModelById(Long id, ModelRequest modelRequest) {

        Model model = modelRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION, id)));

        Product product = productRepository.findById(modelRequest.getProductId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,modelRequest.getProductId())));

        Currency currency = currencyRepository.findById(modelRequest.getCurrencyId()).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.CURRENCY_NOT_FOUND_EXCEPTION));

        if (model.getBuilt_in()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        isSkuUniqueWithId(modelRequest.getSku(),id);

        model.setSku(modelRequest.getSku());
        model.setTitle(modelRequest.getTitle());
        model.setStock_amount(modelRequest.getStock_amount());
        model.setIn_box_quantity(modelRequest.getIn_box_quantity());
        model.setSeq(modelRequest.getSeq());
        model.setBuying_price(modelRequest.getBuying_price());
        model.setIsActive(modelRequest.getIsActive());
        model.setTax_rate(modelRequest.getTax_rate());
        model.setProduct(product);
        model.setCurrency(currency);
        model.setUpdate_at(LocalDateTime.now());
//        model.setImage(null);

        modelRepository.save(model);

        ModelResponse modelResponse = modelMapper.modelToModelResponse(model);
        modelResponse.setProductId(product.getId());
        modelResponse.setCurrencyId(currency.getId());
        return modelResponse;

    }

    private void isSkuUniqueWithId(String sku, Long id) {
        if (sku != null && id != null) {
            Model model= modelRepository.findBySku(sku);
            //Aşağıdaki kod eski sku ile kıyaslamayı engellemek için yazıldı
            if(model!=null && model.getId()!=id)  { throw new BadRequestException(ErrorMessage.NOT_CREATED_SKU_MESSAGE);}
        }
    }

    public void isSkuUnique(String sku) {
            if (sku != null) {
                 Model model= modelRepository.findBySku(sku);
                 if(model!=null)  { throw new BadRequestException(ErrorMessage.NOT_CREATED_SKU_MESSAGE);}
            }
        }





    public ProductPropertyKeyResponse updatePPKeyById(Long id, ProductPropertyKeyRequest productPropertyKeyRequest) {

        ProductPropertyKey productPropertyKey = productPropertyKeyRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));


        if (productPropertyKey.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        productPropertyKey.setName(productPropertyKeyRequest.getName());

        productPropertyKeyRepository.save(productPropertyKey);

        ProductPropertyKeyResponse productPropertyKeyResponse = productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyResponse(productPropertyKey);
        productPropertyKeyResponse.setProductId(productPropertyKey.getProduct().getId());

        return productPropertyKeyResponse;
    }


    public ModelResponse deleteModelById(Long id) {

        Model model = modelRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION,id)));
        if (model.getBuilt_in()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        } //else if () {
        //If any model is deleted, related records in model_property_values, cart_items should be deleted
        //If the model has any related records in offer_items table, it can not be deleted and endpoint returns an error
        //
        else {
            modelRepository.deleteById(id);
        }
        return modelMapper.modelToModelResponse(model);
    }

    public List<ProductPropertyKeyResponse> listPPKeysByProductId(Long id) {

        productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));


        return productPropertyKeyMapper.ppkListToPPKResponseList(
                productPropertyKeyRepository.findByProductId(id));
    }

    //****************************Yardimci Method******************************************
    public List<ModelResponse> getModelsByProductId(Long id) {
        productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));

        List<Model> modelList = modelRepository.findByProductId(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_BY_PRODUCT_ID_EXCEPTION,id)));

        return modelMapper.modelListToModelResponseList(modelList);
    }


}