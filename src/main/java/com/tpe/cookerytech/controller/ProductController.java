package com.tpe.cookerytech.controller;


import com.tpe.cookerytech.dto.request.*;
import com.tpe.cookerytech.dto.request.ProductRequest;
import com.tpe.cookerytech.dto.response.*;
import com.tpe.cookerytech.service.BrandService;
import com.tpe.cookerytech.service.CategoryService;
import com.tpe.cookerytech.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService, BrandService brandService, CategoryService categoryService) {
        this.productService = productService;
    }




    //A01 -> It should return products depending on query and paging parameters Page:26
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<Page<ProductObjectResponse>> getAllProductsWithPage(
            @RequestParam(required = false,defaultValue = "",name="q") String q,
            @RequestParam(required = false,defaultValue = "0",name = "page") int page,
            @RequestParam(required = false,defaultValue = "20",name = "size") int size,
            @RequestParam(required = false,defaultValue = "category",name = "sort") String sort,
            @RequestParam(required = false,defaultValue = "DESC",name = "type") String type){


        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(type), sort));

        Page<ProductObjectResponse> productObjectResponse = productService.allProducts(q,pageable);

        return ResponseEntity.ok(productObjectResponse);


    }


    //A02 -> It should return categories and products whose “featured” field has a value of 1.
    @GetMapping("/featured")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<ProductObjectResponse>> getAllProducts(@RequestParam(required = false,defaultValue = "sequence")String sortFelid,@RequestParam(required = false,defaultValue = "ASC")String sortOrder) {
        Sort sort=Sort.by(Sort.Direction.fromString(sortOrder),sortFelid);
        List<ProductObjectResponse> allProducts = productService.getAllProducts(sort);
        return ResponseEntity.ok(allProducts);
    }



    //A03 -> It should return a product
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){

        ProductResponse productResponse=productService.getProductById(id);

        return ResponseEntity.ok(productResponse);

    }



    //A04 -> It should create a product
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {

        ProductResponse productResponse = productService.createProducts(productRequest);

        return ResponseEntity.ok(productResponse);

    }



    //A05 -> It should update a product
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @RequestBody ProductRequest productRequest) {

        ProductResponse productResponse = productService.updateProductById(id, productRequest);

        return ResponseEntity.ok(productResponse);
    }



    //A06 -> It should delete a product
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductResponse> deleteProductById(@PathVariable Long id){

        ProductResponse productResponse = productService.deleteProductById(id);

        return ResponseEntity.ok(productResponse);

    }



    //A07 -> It should return product property keys of given product
    @GetMapping("/{id}/properties")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ProductPropertyKeyResponse>> getPPKByProductId(@PathVariable Long id){


        List<ProductPropertyKeyResponse> ppkResponseList = productService.listPPKeysByProductId(id);

        return ResponseEntity.ok(ppkResponseList);
    }




    //A08 -> It should create a product property
    @PostMapping("/properties")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductPropertyKeyResponse> createPPKey(@Valid @RequestBody ProductPropertyKeyRequest productPropertyKeyRequest){

        ProductPropertyKeyResponse productPropertyKeyResponse = productService.createPPKey(productPropertyKeyRequest);

        return ResponseEntity.ok(productPropertyKeyResponse);
    }




    //A09 -> It should update a product property
    @PutMapping("/properties/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductPropertyKeyResponse> updatePPKeyById(@PathVariable Long id,
                                                                      @Valid @RequestBody ProductPropertyKeyRequest productPropertyKeyRequest){

        ProductPropertyKeyResponse productPropertyKeyResponse = productService.updatePPKeyById(id, productPropertyKeyRequest);

        return ResponseEntity.ok(productPropertyKeyResponse);

    }



    //A10 -> It should delete a product property
    @DeleteMapping("/properties/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductPropertyKeyResponse> deletePPK(@PathVariable Long id){

        ProductPropertyKeyResponse productPropertyKeyResponse = productService.deletePPKById(id);

        return ResponseEntity.ok(productPropertyKeyResponse);
    }




    //A11 -> It should return models for given products
    @GetMapping("/{id}/models")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ModelGenereteResponse>> getModelsByProductId(@PathVariable Long id,@RequestParam(required = false,defaultValue = "seq") String sortField,@RequestParam(required = false,defaultValue = "ASC") String sortOder) {

        Sort sort=Sort.by(Sort.Direction.fromString(sortOder),sortField);
        List<ModelGenereteResponse> modelResponseList  = productService.getProductsByIdModels(id,sort);

        return ResponseEntity.ok(modelResponseList);

    }




    //A12 -> It should create a model
    @PostMapping("/models")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ModelCreateResponse> createProductModel(@RequestPart("file") MultipartFile file,
                                                            @RequestPart("modelRequest") ModelRequest modelRequest) {

        ModelCreateResponse modelResponse = productService.createProductModels(modelRequest,file);

        return ResponseEntity.ok(modelResponse);
    }




    //A13 -> It should update a model
    @PutMapping("/models/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ModelResponse> updateProductModel(@PathVariable Long id,
                                                            @Valid @RequestBody ModelRequest modelRequest){

        ModelResponse modelResponse = productService.updateProductModelById(id,modelRequest);

        return ResponseEntity.ok(modelResponse);

    }




    //A14 -> It should delete model
    @DeleteMapping("/models/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ModelResponse> deleteProductModel(@PathVariable Long id){

        ModelResponse modelResponse = productService.deleteModelById(id);

        return ResponseEntity.ok(modelResponse);
    }





    //************************************* Helper Methods **********************************************

    @GetMapping("/{id}/get/models")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ModelResponse>> getModelsByProductIdHelper(@PathVariable Long id){


        List<ModelResponse> modelResponseList = productService.getModelsByProductId(id);

        return ResponseEntity.ok(modelResponseList);
    }

}
