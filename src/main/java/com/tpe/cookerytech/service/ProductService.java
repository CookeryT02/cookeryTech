package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.domain.Currency;
import com.tpe.cookerytech.dto.request.ModelRequest;
import com.tpe.cookerytech.dto.request.ProductPropertyKeyRequest;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.*;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ConflictException;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.*;
import com.tpe.cookerytech.repository.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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
    private final OfferItemRepository offerItemRepository;
    private final ModelRepository modelRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ImageFileRepository imageFileRepository;
    private final BrandMapper brandMapper;
    private final CategoryMapper categoryMapper;

    private final ModelPropertyValueRepository modelPropertyValueRepository;



    public ProductService(ProductRepository productRepository, ProductMapper productMapper, BrandService brandService, CategoryService categoryService, CurrencyRepository currencyRepository, ModelMapper modelMapper, ProductPropertyKeyMapper productPropertyKeyMapper, ProductPropertyKeyRepository productPropertyKeyRepository, OfferItemRepository offerItemRepository, ModelRepository modelRepository, ShoppingCartItemRepository shoppingCartItemRepository, ImageFileRepository imageFileRepository, BrandMapper brandMapper, CategoryMapper categoryMapper, ModelPropertyValueRepository modelPropertyValueRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.currencyRepository = currencyRepository;
        this.modelMapper = modelMapper;
        this.productPropertyKeyMapper = productPropertyKeyMapper;
        this.productPropertyKeyRepository = productPropertyKeyRepository;
        this.offerItemRepository = offerItemRepository;
        this.modelRepository = modelRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.imageFileRepository = imageFileRepository;
        this.brandMapper = brandMapper;
        this.categoryMapper = categoryMapper;
        this.modelPropertyValueRepository = modelPropertyValueRepository;
    }


    //A01
    public Page<ProductObjectResponse> allProducts(String q ,Pageable pageable) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Sort sort =pageable.getSort();

        if (authentication != null || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))
                || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PRODUCT_MANAGER"))
                || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SALES_SPECIALIST"))
                || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SALES_MANAGER"))
                || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
        ) {


            List<Product> productList = (productRepository.findByIsActive(true,sort));
            List<Product> filteredProductsCustomer =productList.stream()
                    .filter(p -> {
                        Brand brand = p.getBrand();
                        Category category = p.getCategory();
                        return brand.getIsActive() && category.getIsActive();
                    })
                    .collect(Collectors.toList());

            Page<Product> p = new PageImpl<Product>(productList);
            Page<Product> f = new PageImpl<Product>(filteredProductsCustomer);



            Page<Product> productPages = productRepository.getAllProductsIsActiveTrue(q, pageable);


            Page<ProductObjectResponse> productObjectResponse = productPages.map(product -> {

                ProductObjectResponse productObjectResponses = new ProductObjectResponse();
                productObjectResponses.setBrand(brandMapper.brandToBrandResponse(product.getBrand()));
                productObjectResponses.setCategory(categoryMapper.categoryToCategoryResponse(product.getCategory()));

                return productObjectResponses;
            });


            return productPages.map(productMapper::productToProductObjectResponse);




        } else {





            List<Product> productList = (productRepository.findByIsActive(false,sort));
            List<Product> filteredProducts =productList.stream()
                    .filter(p -> {
                        Brand brand = p.getBrand();
                        Category category = p.getCategory();
                        return brand.getIsActive() && category.getIsActive();
                    })
                    .collect(Collectors.toList());

            Page<Product> p = new PageImpl<Product>(productList);
            Page<Product> f = new PageImpl<Product>(filteredProducts);

            Page<Product> productPages = productRepository.getAllProductsIsActiveFalse(q, pageable);


            Page<ProductObjectResponse> productObjectResponse = productPages.map(product -> {

                ProductObjectResponse productObjectResponses = new ProductObjectResponse();
                productObjectResponses.setBrand(brandMapper.brandToBrandResponse(product.getBrand()));
                productObjectResponses.setCategory(categoryMapper.categoryToCategoryResponse(product.getCategory()));

                return productObjectResponses;
            });


            return productPages.map(productMapper::productToProductObjectResponse);




        }





    }




    //A02
    public List<ProductObjectResponse> getAllProducts(Sort sort) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            List<Product> productList = productRepository.findAll(sort);

            List<ProductObjectResponse> listObjectResponse=productMapper.productsToProductObjectResponses(productList);
            return listObjectResponse;

        } else {
            List<Product> productList = (productRepository.findByIsActive(true,sort));
            List<Product> filteredProducts =productList.stream()
                    .filter(p -> {
                        Brand brand = p.getBrand();
                        Category category = p.getCategory();
                        System.out.println(p.getCategory().getIsActive());
                        return p.getIsFeatured() && brand != null && category != null && brand.getIsActive() && category.getIsActive();
                    }).collect(Collectors.toList());
            return productMapper.productsToProductObjectResponses(filteredProducts);
        }
    }



    //A03
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));


        List<Model> models = modelRepository.findByProductId(id).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION));

        //Product altindaki ilk modelin image ini product a setleme
        Set<ImageFile> modelImages = models.get(0).getImage();
        ImageFile imageFile = modelImages.iterator().next();


        ProductResponse productResponse = productMapper.productToProductResponse(product);
        productResponse.setBrandId(product.getBrand().getId());
        productResponse.setCategoryId(product.getCategory().getId());
        productResponse.setImage(ImageFileService.convertToResponse(imageFile));

        return productResponse;
    }



    //A04
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



    //A05
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
        product.setUpdatedAt(LocalDateTime.now());
        product.setSequence(productRequest.getSequence());
        product.setBuiltIn(productRequest.getBuiltIn());

        productRepository.save(product);

        ProductResponse productResponse = productMapper.productToProductResponse(product);
        productResponse.setBrandId(product.getBrand().getId());
        productResponse.setCategoryId(product.getCategory().getId());
        return productResponse;
    }



    //A06
    public ProductResponse deleteProductById (Long id){

        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));


        if (product.getBuiltIn()) {
            throw new BadRequestException(String.format(ErrorMessage.PRODUCT_CANNOT_DELETE_EXCEPTION, id));
        }

        if (!offerItemRepository.findByProductId(id).isEmpty()) {
            throw new BadRequestException(String.format(ErrorMessage.PRODUCT_CANNOT_DELETE_EXCEPTION, id));
        }

        deleteRelatedRecords(id);

        productRepository.deleteById(id);

        return productMapper.productToProductResponse(product);

    }



    //A07
    public List<ProductPropertyKeyResponse> listPPKeysByProductId(Long id) {

        productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));

            List<ProductPropertyKey> productPropertyKeys= productPropertyKeyRepository.findByProductId(id);
            productPropertyKeys.sort(Comparator.comparingInt(ProductPropertyKey::getSeq));

        return productPropertyKeyMapper.ppkListToPPKResponseList(productPropertyKeys);
    }



    //A08
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
        ModelPropertyValue modelPropertyValue = new ModelPropertyValue();
        modelPropertyValue.setProductPropertyKey(productPropertyKey);
        modelPropertyValueRepository.save(modelPropertyValue);

        ProductPropertyKeyResponse productPropertyKeyResponse = productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyResponse(productPropertyKey);
        productPropertyKeyResponse.setProductId(product.getId());

        return productPropertyKeyResponse;
    }




    //A09
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




    //A10
    public ProductPropertyKeyResponse deletePPKById(Long id) {
        ProductPropertyKey productPropertyKey = productPropertyKeyRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_PROPERTY_KEY_NOT_FOUND,id)));

        ModelPropertyValue modelPropertyValue = modelPropertyValueRepository.findByProductPropertyKey(productPropertyKey);

        if (productPropertyKey.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }else if (!(modelPropertyValue==null)) {
            throw new BadRequestException(ErrorMessage.MODEL_PROPERY_VALUE_CAN_NOT_DELETE);
        }else {
            productPropertyKeyRepository.deleteById(id);
        }
        ProductPropertyKeyResponse productPropertyKeyResponse = productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyResponse(productPropertyKey);
        productPropertyKeyResponse.setProductId(productPropertyKey.getProduct().getId());
        return productPropertyKeyResponse;
    }




    //A11
    public List<ModelResponse> getProductsByIdModels(Long id,Sort sort) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {

            List<Product> productList = (productRepository.findByIsActive(false,sort));
            List<Product> filteredProductsCustomer =productList.stream()
                    .filter(p -> {
                        Brand brand = p.getBrand();
                        Category category = p.getCategory();
                        System.out.println(p.getCategory().getIsActive());
                        return p.getIsFeatured() && brand != null && category != null && brand.getIsActive() && category.getIsActive();
                    })
                    .collect(Collectors.toList());

            List<Model> modelList = (modelRepository.findByIsActive(false));
            List<Model> filteredModelsCustomer = modelList.stream()
                    .filter(m -> {
                        Product product = m.getProduct();
                        return product != null && product.getIsActive();
                    })
                    .collect(Collectors.toList());


            productRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));

            List<Model> modelLists = modelRepository.findByProductId(id).orElseThrow(()->
                    new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_BY_PRODUCT_ID_EXCEPTION,id)));


//            List<Model> sortedModelList = modelLists.stream()
//                    .sorted(Comparator.comparingInt(Model::getSeq).thenComparing(Model::getTitle))
//                    .collect(Collectors.toList());

            return modelMapper.modelListToModelResponseList(modelLists);

        } else {

            List<Product> productList = (productRepository.findByIsActive(true,sort));
            List<Product> filteredProducts =productList.stream()
                    .filter(p -> {
                        Brand brand = p.getBrand();
                        Category category = p.getCategory();
                        System.out.println(p.getCategory().getIsActive());
                        return p.getIsFeatured() && brand != null && category != null && brand.getIsActive() && category.getIsActive();
                    }).collect(Collectors.toList());

            List<Model> modelList = (modelRepository.findByIsActive(false));

            List<Model> filteredModelsCustomer = modelList.stream()
                    .filter(m -> {
                        Product product = m.getProduct();
                        return product != null && product.getIsActive();
                    }).collect(Collectors.toList());

            productRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));

            List<Model> modelLists = modelRepository.findByProductId(id).orElseThrow(()->
                    new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_BY_PRODUCT_ID_EXCEPTION,id)));

//            List<Model> sortedModelList = modelLists.stream()
//                    .sorted(Comparator.comparingInt(Model::getSeq).thenComparing(Model::getTitle))
//                    .collect(Collectors.toList());

            return modelMapper.modelListToModelResponseList(modelLists);
        }
    }




    //A12
    public ModelCreateResponse createProductModels(ModelRequest modelRequest, MultipartFile file) {
        isSkuUnique(modelRequest.getSku());

        ImageFile imageFile = null;

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            ImageData imageData = new ImageData(file.getBytes());
            imageFile = new ImageFile(fileName,file.getContentType(),imageData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        Model model = modelMapper.modelRequestToModel(modelRequest);
        imageFile.setModel(model);
        imageFileRepository.save(imageFile);


        Product product= productRepository.findById(modelRequest.getProductId()).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,modelRequest.getProductId())));

        Currency currency = currencyRepository.findById(modelRequest.getCurrencyId()).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.CURRENCY_NOT_FOUND_EXCEPTION));

        Set<ImageFile> imageFiles =new HashSet<>();
        imageFiles.add(imageFile);

        Set<ImageFileResponse> imageFileResponses =new HashSet<>();
        imageFileResponses.add(ImageFileService.convertToResponse(imageFile));


        model.setSku(modelRequest.getSku());
        model.setProduct(product);
        model.setCurrency(currency);
        model.setImage(imageFiles);
        model.setCreateAt(LocalDateTime.now());


        List<ProductPropertyKey> productPropertyKeyList = productPropertyKeyRepository.findByProductId(modelRequest.getProductId());

        if (!productPropertyKeyList.isEmpty()) {
            Map<String,String> modelProperties = new HashMap<>();
            int i=0;
            for (ProductPropertyKey propertyKey : productPropertyKeyList) {
                ModelPropertyValue modelPropertyValue = modelPropertyValueRepository.findByProductPropertyKey(propertyKey);
                modelPropertyValue.setModel(model);
                List<String> modelPropertyValues = modelRequest.getModelPropertyValue();

                if (!modelPropertyValues.isEmpty() && modelPropertyValue != null) {
                    if (i <= modelPropertyValues.size()) {
                        modelPropertyValue.setValue(modelPropertyValues.get(i));
                    }
                    modelPropertyValueRepository.save(modelPropertyValue);
                    modelProperties.put(propertyKey.getName(),modelPropertyValue.getValue());
                    model.setProperties(modelProperties);
                }
                i++;
            }
        }


        modelRepository.save(model);

        ModelCreateResponse modelResponse = modelMapper.modelToModelCreateResponse(model);
        modelResponse.setCreate_at(model.getCreateAt());
        modelResponse.setProductId(product.getId());
        modelResponse.setCurrencyId(currency.getId());
        modelResponse.setImage(imageFileResponses);
        modelResponse.setProperties(model.getProperties());
        return modelResponse;
    }




    //A13
    public ModelResponse updateProductModelById(Long id, ModelRequest modelRequest) {

        Model model = modelRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION, id)));

        Product product = productRepository.findById(modelRequest.getProductId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,modelRequest.getProductId())));

        Currency currency = currencyRepository.findById(modelRequest.getCurrencyId()).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.CURRENCY_NOT_FOUND_EXCEPTION));

        if (model.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        isSkuUniqueWithId(modelRequest.getSku(),id);

        model.setSku(modelRequest.getSku());
        model.setTitle(modelRequest.getTitle());
        model.setStockAmount(modelRequest.getStock_amount());
        model.setInBoxQuantity(modelRequest.getIn_box_quantity());
        model.setSeq(modelRequest.getSeq());
        model.setBuyingPrice(modelRequest.getBuying_price());
        model.setIsActive(modelRequest.getIsActive());
        model.setTaxRate(modelRequest.getTax_rate());
        model.setProduct(product);
        model.setCurrency(currency);
        model.setUpdateAt(LocalDateTime.now());

        modelRepository.save(model);

        ModelResponse modelResponse = modelMapper.modelToModelResponse(model);
        modelResponse.setProductId(product.getId());
        modelResponse.setCurrencyId(currency.getId());
        return modelResponse;
    }




    //A14
    public ModelResponse deleteModelById(Long id) {

        Model model = modelRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION,id)));
        if (model.getBuiltIn()) {
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


    //************************************* Helper Methods **********************************************

    private void deleteRelatedRecords(Long productId) {

        modelRepository.deleteByProductId(productId);

        shoppingCartItemRepository.deleteByProductId(productId);

    }

    private void isSkuUniqueWithId(String sku, Long id) {
        if (sku != null && id != null) {
            Model model= modelRepository.findBySku(sku);
            //Aşağıdaki kod eski sku ile kıyaslamayı engellemek için yazıldı
            if(model!=null && !Objects.equals(model.getId(), id))  { throw new BadRequestException(ErrorMessage.NOT_CREATED_SKU_MESSAGE);}
        }
    }

    public void isSkuUnique(String sku) {
            if (sku != null) {
                 Model model= modelRepository.findBySku(sku);
                 if(model!=null)  { throw new BadRequestException(ErrorMessage.NOT_CREATED_SKU_MESSAGE);}
            }
    }


    public List<ModelResponse> getModelsByProductId(Long id) {
        productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.PRODUCT_NOT_FOUND_EXCEPTION,id)));

        List<Model> modelList = modelRepository.findByProductId(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_BY_PRODUCT_ID_EXCEPTION,id)));
//        sort by seq and title
        modelList.sort(Comparator.comparingInt(Model::getSeq).thenComparing(Model::getTitle));

        return modelMapper.modelListToModelResponseList(modelList);
    }
}